package gov.nih.nci.ctcae.core.domain.security.passwordpolicy;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

import gov.nih.nci.ctcae.core.validation.annotation.Validatable;
import gov.nih.nci.ctcae.core.domain.security.passwordpolicy.LoginPolicy;
import gov.nih.nci.ctcae.core.domain.security.passwordpolicy.PasswordCreationPolicy;
import gov.nih.nci.ctcae.core.domain.BasePersistable;
import gov.nih.nci.cabig.ctms.domain.AbstractMutableDomainObject;


@Entity
@Table(name = "password_policy")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_password_policy_id")})
@Validatable
public class PasswordPolicy extends BasePersistable {
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private static final long TOKEN_TIMEOUT_MS = 48 * 60 * 60 * 1000;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "minPasswordAge", column = @Column(name = "cn_min_age")),
            @AttributeOverride(name = "passwordHistorySize", column = @Column(name = "cn_history_size")),
            @AttributeOverride(name = "minPasswordLength", column = @Column(name = "cn_min_length")),
            @AttributeOverride(name = "combinationPolicy.minimumRequired", column = @Column(name = "cn_cb_min_required")),
            @AttributeOverride(name = "combinationPolicy.upperCaseAlphabetRequired", column = @Column(name = "cn_cb_is_upper_case_alphabet")),
            @AttributeOverride(name = "combinationPolicy.lowerCaseAlphabetRequired", column = @Column(name = "cn_cb_is_lower_case_alphabet")),
            @AttributeOverride(name = "combinationPolicy.nonAlphaNumericRequired", column = @Column(name = "cn_cb_is_non_alpha_numeric")),
            @AttributeOverride(name = "combinationPolicy.baseTenDigitRequired", column = @Column(name = "cn_cb_is_base_ten_digit")),
            @AttributeOverride(name = "combinationPolicy.maxSubstringLength", column = @Column(name = "cn_cb_max_substring_length"))})
    private PasswordCreationPolicy passwordCreationPolicy;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "allowedFailedLoginAttempts", column = @Column(name = "ln_allowed_attempts")),
            @AttributeOverride(name = "lockOutDuration", column = @Column(name = "ln_lockout_duration")),
            @AttributeOverride(name = "maxPasswordAge", column = @Column(name = "ln_max_age"))})
    private LoginPolicy loginPolicy;

    public PasswordCreationPolicy getPasswordCreationPolicy() {
        return passwordCreationPolicy;
    }

    public void setPasswordCreationPolicy(PasswordCreationPolicy passwordCreationPolicy) {
        this.passwordCreationPolicy = passwordCreationPolicy;
    }

    public LoginPolicy getLoginPolicy() {
        return loginPolicy;
    }

    public void setLoginPolicy(LoginPolicy loginPolicy) {
        this.loginPolicy = loginPolicy;
    }

    @Transient
    public long getTokenTimeout() {
        return TOKEN_TIMEOUT_MS;
    }

    public String toString() {
        return loginPolicy.toString() + "\n" + passwordCreationPolicy.toString();
    }
}
