
package jstellarapi.ds.tx;

import java.math.BigDecimal;

import javax.annotation.Generated;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Amount {

    @Expose
    private String currency;
    @Expose
    private String issuer;
    @Expose
    private BigDecimal value;

    /**
     * 
     * @return
     *     The currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * 
     * @param currency
     *     The currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Amount withCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    /**
     * 
     * @return
     *     The issuer
     */
    public String getIssuer() {
        return issuer;
    }

    /**
     * 
     * @param issuer
     *     The issuer
     */
    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public Amount withIssuer(String issuer) {
        this.issuer = issuer;
        return this;
    }

    /**
     * 
     * @return
     *     The value
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * 
     * @param value
     *     The value
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Amount withValue(BigDecimal value) {
        this.value = value;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(currency).append(issuer).append(value).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Amount) == false) {
            return false;
        }
        Amount rhs = ((Amount) other);
        return new EqualsBuilder().append(currency, rhs.currency).append(issuer, rhs.issuer).append(value, rhs.value).isEquals();
    }

}
