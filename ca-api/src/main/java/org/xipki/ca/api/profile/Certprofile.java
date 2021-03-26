/*
 *
 * Copyright (c) 2013 - 2020 Lijun Liao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.xipki.ca.api.profile;

import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.CertificatePolicies;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.xipki.ca.api.BadCertTemplateException;
import org.xipki.ca.api.BadFormatException;
import org.xipki.ca.api.PublicCaInfo;
import org.xipki.security.KeyUsage;
import org.xipki.security.SignAlgo;
import org.xipki.util.Args;
import org.xipki.util.CollectionUtil;
import org.xipki.util.StringUtil;
import org.xipki.util.Validity;

import java.io.Closeable;
import java.util.*;

/**
 * Defines how the certificate looks like. All Certprofile classes must extend this class.
 *
 * @author Lijun Liao
 * @since 2.0.0
 */

public abstract class Certprofile implements Closeable {

  public static class AuthorityInfoAccessControl {

    private final boolean includesCaIssuers;

    private final boolean includesOcsp;

    private final Set<String> ocspProtocols;

    private final Set<String> caIssuersProtocols;

    public AuthorityInfoAccessControl(boolean includesCaIssuers, boolean includesOcsp,
        Set<String> caIssuersProtocols, Set<String> ocspProtocols) {
      this.includesCaIssuers = includesCaIssuers;
      this.includesOcsp = includesOcsp;
      this.ocspProtocols = ocspProtocols == null
          ? null : Collections.unmodifiableSet(new HashSet<>(ocspProtocols));
      this.caIssuersProtocols = caIssuersProtocols == null
          ? null : Collections.unmodifiableSet(new HashSet<>(caIssuersProtocols));
    }

    public boolean isIncludesCaIssuers() {
      return includesCaIssuers;
    }

    public boolean isIncludesOcsp() {
      return includesOcsp;
    }

    public Set<String> getOcspProtocols() {
      return ocspProtocols;
    }

    public Set<String> getCaIssuersProtocols() {
      return caIssuersProtocols;
    }

  } // class AuthorityInfoAccessControl

  public static class CrlDistributionPointsControl {

    private final Set<String> protocols;

    public CrlDistributionPointsControl(Set<String> protocols) {
      this.protocols = protocols == null
         ? null : Collections.unmodifiableSet(new HashSet<>(protocols));
    }

    public Set<String> getProtocols() {
      return protocols;
    }

  } // class CrlDistributionPointsControl

  public static enum CertDomain {
    RFC5280,
    CABForumBR
  } // class CertDomain

  public enum CertLevel {
    RootCA,
    SubCA,
    EndEntity
  } // class CertLevel

  public static class ExtensionControl {

    private final boolean critical;

    private final boolean required;

    private final boolean request;

    public ExtensionControl(boolean critical, boolean required, boolean request) {
      this.critical = critical;
      this.required = required;
      this.request = request;
    }

    public boolean isCritical() {
      return critical;
    }

    public boolean isRequired() {
      return required;
    }

    public boolean isRequest() {
      return request;
    }

  } // class CertLevel

  public static class ExtKeyUsageControl {

    private final ASN1ObjectIdentifier extKeyUsage;

    private final boolean required;

    public ExtKeyUsageControl(ASN1ObjectIdentifier extKeyUsage, boolean required) {
      this.extKeyUsage = Args.notNull(extKeyUsage, "extKeyUsage");
      this.required = required;
    }

    public ASN1ObjectIdentifier getExtKeyUsage() {
      return extKeyUsage;
    }

    public boolean isRequired() {
      return required;
    }

  } // class ExtKeyUsageControl

  public enum GeneralNameTag {

    otherName(0),
    rfc822Name(1),
    DNSName(2),
    x400Adress(3),
    directoryName(4),
    ediPartyName(5),
    uniformResourceIdentifier(6),
    IPAddress(7),
    registeredID(8);

    private final int tag;

    private GeneralNameTag(int tag) {
      this.tag = tag;
    }

    public int getTag() {
      return tag;
    }

  } // class GeneralNameTag

  public static class GeneralNameMode {

    private final GeneralNameTag tag;

    // not applied to all tags, currently only for tag otherName
    private final Set<ASN1ObjectIdentifier> allowedTypes;

    public GeneralNameMode(GeneralNameTag tag) {
      this.tag = Args.notNull(tag, "tag");
      this.allowedTypes = null;
    }

    public GeneralNameMode(GeneralNameTag tag, Set<ASN1ObjectIdentifier> allowedTypes) {
      this.tag = Args.notNull(tag, "tag");
      this.allowedTypes = CollectionUtil.isEmpty(allowedTypes) ? Collections.emptySet()
          : CollectionUtil.unmodifiableSet(allowedTypes);
    }

    public GeneralNameTag getTag() {
      return tag;
    }

    public Set<ASN1ObjectIdentifier> getAllowedTypes() {
      return allowedTypes;
    }

  } // class GeneralNameMode

  public static class KeyUsageControl {

    private final KeyUsage keyUsage;

    private final boolean required;

    public KeyUsageControl(KeyUsage keyUsage, boolean required) {
      this.keyUsage = Args.notNull(keyUsage, "keyUsage");
      this.required = required;
    }

    public KeyUsage getKeyUsage() {
      return keyUsage;
    }

    public boolean isRequired() {
      return required;
    }

  } // class KeyUsageControl

  public static class RdnControl {

    private final int minOccurs;

    private final int maxOccurs;

    private final ASN1ObjectIdentifier type;

    private TextVadidator pattern;

    private StringType stringType;

    private Range stringLengthRange;

    private String prefix;

    private String suffix;

    private String value;

    private boolean valueOverridable;

    private String group;

    /**
     * This RDN is for other purpose, will not contained in the Subject field of certificate.
     */
    private boolean notInSubject;

    public RdnControl(ASN1ObjectIdentifier type) {
      this(type, 1, 1);
    }

    public RdnControl(ASN1ObjectIdentifier type, String value, boolean valueOverridable) {
      this.type = Args.notNull(type, "type");
      this.minOccurs = 1;
      this.maxOccurs = 1;
      if (StringUtil.isBlank(value)) {
        this.value = null;
        this.valueOverridable = true;
      } else {
        this.value = value;
        this.valueOverridable = valueOverridable;
      }
    }

    public RdnControl(ASN1ObjectIdentifier type, int minOccurs, int maxOccurs) {
      if (minOccurs < 0 || maxOccurs < 1 || minOccurs > maxOccurs) {
        throw new IllegalArgumentException(
            String.format("illegal minOccurs=%s, maxOccurs=%s", minOccurs, maxOccurs));
      }

      this.type = Args.notNull(type, "type");
      this.minOccurs = minOccurs;
      this.maxOccurs = maxOccurs;
      this.valueOverridable = true;
    }

    public int getMinOccurs() {
      return minOccurs;
    }

    public int getMaxOccurs() {
      return maxOccurs;
    }

    public ASN1ObjectIdentifier getType() {
      return type;
    }

    public StringType getStringType() {
      return stringType;
    }

    public TextVadidator getPattern() {
      return pattern;
    }

    public Range getStringLengthRange() {
      return stringLengthRange;
    }

    public void setStringType(StringType stringType) {
      this.stringType = stringType;
    }

    public void setStringLengthRange(Range stringLengthRange) {
      this.stringLengthRange = stringLengthRange;
    }

    public void setPattern(TextVadidator pattern) {
      this.pattern = pattern;
    }

    public String getPrefix() {
      return prefix;
    }

    public void setPrefix(String prefix) {
      this.prefix = prefix;
    }

    public String getSuffix() {
      return suffix;
    }

    public void setSuffix(String suffix) {
      this.suffix = suffix;
    }

    public String getGroup() {
      return group;
    }

    public void setGroup(String group) {
      this.group = group;
    }

    public String getValue() {
      return value;
    }

    public boolean isValueOverridable() {
      return valueOverridable;
    }

    public boolean isNotInSubject() {
      return notInSubject;
    }

    public void setNotInSubject(boolean notInSubject) {
      this.notInSubject = notInSubject;
    }

  } // class RdnControl

  public enum StringType {

    teletexString,
    printableString,
    utf8String,
    bmpString,
    ia5String;

    public ASN1Encodable createString(String text) {
      Args.notNull(text, "text");

      if (teletexString == this) {
        return new DERT61String(text);
      } else if (printableString == this) {
        return new DERPrintableString(text);
      } else if (utf8String == this) {
        return new DERUTF8String(text);
      } else if (bmpString == this) {
        return new DERBMPString(text);
      } else if (ia5String == this) {
        return new DERIA5String(text, true);
      } else {
        throw new IllegalStateException("should not reach here, unknown StringType " + this.name());
      }
    }

  } // class StringType

  public static class SubjectControl {

    private final Map<ASN1ObjectIdentifier, RdnControl> controls;

    private final Map<ASN1ObjectIdentifier, String> typeGroups;

    private final Map<String, Set<ASN1ObjectIdentifier>> groupTypes;

    private final Set<String> groups;

    private final List<ASN1ObjectIdentifier> types;

    public SubjectControl(List<RdnControl> controls, boolean keepRdnOrder) {
      Args.notEmpty(controls, "controls");
      this.typeGroups = new HashMap<>();

      List<ASN1ObjectIdentifier> sortedOids = new ArrayList<>(controls.size());
      if (keepRdnOrder) {
        for (RdnControl m : controls) {
          sortedOids.add(m.getType());
        }
      } else {
        Set<ASN1ObjectIdentifier> oidSet = new HashSet<>();
        for (RdnControl m : controls) {
          oidSet.add(m.getType());
        }

        List<ASN1ObjectIdentifier> oids = SubjectDnSpec.getForwardDNs();

        for (ASN1ObjectIdentifier oid : oids) {
          if (oidSet.contains(oid)) {
            sortedOids.add(oid);
          }
        }

        for (ASN1ObjectIdentifier oid : oidSet) {
          if (!sortedOids.contains(oid)) {
            sortedOids.add(oid);
          }
        }
      }

      this.types = Collections.unmodifiableList(sortedOids);

      Set<String> groupSet = new HashSet<>();
      this.groupTypes = new HashMap<>();
      this.controls = new HashMap<>();

      for (RdnControl control : controls) {
        ASN1ObjectIdentifier type = control.getType();
        this.controls.put(type, control);
        String group = control.getGroup();
        if (StringUtil.isBlank(group)) {
          continue;
        }

        groupSet.add(group);
        typeGroups.put(type, group);
        Set<ASN1ObjectIdentifier> typeSet = groupTypes.get(group);
        if (typeSet == null) {
          typeSet = new HashSet<>();
          groupTypes.put(group, typeSet);
        }
        typeSet.add(type);
      }

      this.groups = Collections.unmodifiableSet(groupSet);
    } // constructor

    public RdnControl getControl(ASN1ObjectIdentifier type) {
      Args.notNull(type, "type");
      return controls.isEmpty() ? SubjectDnSpec.getRdnControl(type) : controls.get(type);
    }

    public String getGroup(ASN1ObjectIdentifier type) {
      return typeGroups.get(Args.notNull(type, "type"));
    }

    public Set<ASN1ObjectIdentifier> getTypesForGroup(String group) {
      return groupTypes.get(Args.notNull(group, "group"));
    }

    public Set<String> getGroups() {
      return groups;
    }

    public List<ASN1ObjectIdentifier> getTypes() {
      return types;
    }

  } // class SubjectControl

  public static class SubjectInfo {

    private final X500Name grantedSubject;

    private final String warning;

    public SubjectInfo(X500Name grantedSubject, String warning) {
      this.grantedSubject = Args.notNull(grantedSubject, "grantedSubject");
      this.warning = warning;
    }

    public X500Name getGrantedSubject() {
      return grantedSubject;
    }

    public String getWarning() {
      return warning;
    }

  }

  public enum X509CertVersion {

    v1(1),
    v2(2),
    v3(3);

    private int versionNumber;

    X509CertVersion(int versionNumber) {
      this.versionNumber = versionNumber;
    }

    public int getVersionNumber() {
      return versionNumber;
    }

    public static X509CertVersion forName(String version) {
      Args.notNull(version, "version");

      for (X509CertVersion m : values()) {
        if (m.name().equalsIgnoreCase(version)) {
          return m;
        }
      }
      throw new IllegalArgumentException("invalid X509CertVersion " + version);
    }

    public static X509CertVersion forValue(int versionNumber) {
      for (X509CertVersion m : values()) {
        if (m.versionNumber == versionNumber) {
          return m;
        }
      }
      throw new IllegalArgumentException("invalid X509CertVersion " + versionNumber);
    }

  } // class SubjectInfo

  public boolean isOnlyForRa() {
    return false;
  }

  protected Certprofile() {
  }

  @Override
  public void close() {
  }

  public X509CertVersion getVersion() {
    return X509CertVersion.v3;
  }

  public List<SignAlgo> getSignatureAlgorithms() {
    return null;
  }

  /**
   * Returns whether use subject and serial number of the issuer certificate in the
   * AuthorityKeyIdentifier extension.
   *
   * @return whether include subject and serial number of the issuer certificate in the
   *         AuthorityKeyIdentifier extension.
   */
  public boolean useIssuerAndSerialInAki() {
    return false;
  }

  /**
   * Get the SubjectControl.
   *
   * @return the SubjectControl, may not be <code>null</code>.
   */
  public abstract SubjectControl getSubjectControl();

  public abstract AuthorityInfoAccessControl getAiaControl();

  public abstract CrlDistributionPointsControl getCrlDpControl();

  public abstract CrlDistributionPointsControl getFreshestCrlControl();

  public abstract CertificatePolicies getCertificatePolicies();

  public abstract Set<GeneralNameMode> getSubjectAltNameModes();

  /**
   * Increments the SerialNumber attribute in the subject.
   * @param currentSerialNumber
   *          Current serial number. Could be {@code null}.
   * @return the incremented serial number
   * @throws BadFormatException
   *         If the currentSerialNumber is not a non-negative decimal long.
   */
  public String incSerialNumber(String currentSerialNumber)
      throws BadFormatException {
    try {
      long currentSn = (currentSerialNumber == null) ? 0
          : Long.parseLong(currentSerialNumber.trim());
      if (currentSn < 0) {
        throw new BadFormatException("invalid currentSerialNumber " + currentSerialNumber);
      }
      return Long.toString(currentSn + 1);
    } catch (NumberFormatException ex) {
      throw new BadFormatException(String.format(
          "invalid serialNumber attribute %s", currentSerialNumber));
    }
  }

  /**
   * Whether the subject attribute serialNumber in request is permitted.
   *
   * @return whether the serialNumber is permitted in request.
   */
  public boolean isSerialNumberInReqPermitted() {
    return true;
  }

  public Set<ExtKeyUsageControl> getExtendedKeyUsages() {
    return null;
  }

  /**
   * Returns the SubjectInfoAccess modes.
   * Use the dummy oid 0.0.0.0 to identify the NULL accessMethod.
   *
   * @return the SubjectInfoAccess modes.
   */
  public Map<ASN1ObjectIdentifier, Set<GeneralNameMode>> getSubjectInfoAccessModes() {
    return null;
  }

  public abstract Map<ASN1ObjectIdentifier, ExtensionControl> getExtensionControls();

  /**
   * Initializes this object.
   *
   * @param data
   *          Configuration. Could be {@code null}.
   * @throws CertprofileException
   *         if error during the initialization occurs.
   */
  public abstract void initialize(String data)
      throws CertprofileException;

  public abstract CertLevel getCertLevel();

  public abstract CertDomain getCertDomain();

  public KeypairGenControl getKeypairGenControl() {
    return KeypairGenControl.ForbiddenKeypairGenControl.INSTANCE;
  }

  public abstract Map<ASN1ObjectIdentifier, KeyParametersOption> getKeyAlgorithms();

  public abstract Set<KeyUsageControl> getKeyUsage();

  public abstract Integer getPathLenBasicConstraint();

  /**
   * Checks and gets the granted NotBefore.
   *
   * @param notBefore
   *          Requested NotBefore. Could be {@code null}.
   * @return the granted NotBefore.
   */
  public abstract Date getNotBefore(Date notBefore);

  public abstract Validity getValidity();

  /**
   * Checks the public key. If the check passes, returns the canonicalized public key.
   *
   * @param publicKey
   *          Requested public key. Must not be {@code null}.
   * @return the granted public key.
   * @throws BadCertTemplateException
   *         if the publicKey does not have correct format or is not permitted.
   * @throws CertprofileException
   *         if error occurs.
   */
  public abstract SubjectPublicKeyInfo checkPublicKey(SubjectPublicKeyInfo publicKey)
      throws CertprofileException, BadCertTemplateException;

  /**
   * Checks the requested subject. If the check passes, returns the canonicalized subject.
   *
   * @param requestedSubject
   *          Requested subject. Must not be {@code null}.
   * @return the granted subject
   * @throws BadCertTemplateException
   *         if the subject is not permitted.
   * @throws CertprofileException
   *         if error occurs.
   */
  public abstract SubjectInfo getSubject(X500Name requestedSubject)
      throws CertprofileException, BadCertTemplateException;

  /**
   * Checks the requested extensions and returns the canonicalized ones.
   *
   * @param extensionControls
   *          Extension controls.
   * @param requestedSubject
   *          Requested subject. Must not be {@code null}.
   * @param grantedSubject
   *          Granted subject. Must not be {@code null}.
   * @param requestedExtensions
   *          Requested extensions. Could be {@code null}.
   * @param notBefore
   *          NotBefore. Must not be {@code null}.
   * @param notAfter
   *          NotAfter. Must not be {@code null}.
   * @param caInfo
   *          CA information.
   * @return extensions of the certificate to be issued.
   * @throws BadCertTemplateException
   *         if at least one of extension is not permitted.
   * @throws CertprofileException
   *         if error occurs.
   */
  public abstract ExtensionValues getExtensions(
      Map<ASN1ObjectIdentifier, ExtensionControl> extensionControls, X500Name requestedSubject,
      X500Name grantedSubject, Map<ASN1ObjectIdentifier, Extension> requestedExtensions,
      Date notBefore, Date notAfter, PublicCaInfo caInfo)
          throws CertprofileException, BadCertTemplateException;

  /**
   * Returns maximal size in bytes of the certificate.
   *
   * @return maximal size in bytes of the certificate, 0 or negative value
   *         indicates accepting all sizes.
   */
  public int getMaxCertSize() {
    return 0;
  }

}
