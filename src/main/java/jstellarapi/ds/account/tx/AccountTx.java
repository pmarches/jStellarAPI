
package jstellarapi.ds.account.tx;

import javax.annotation.Generated;
import javax.validation.Valid;
import com.google.gson.annotations.Expose;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class AccountTx {

    @Expose
    @Valid
    private Result result;

    /**
     * 
     * @return
     *     The result
     */
    public Result getResult() {
        return result;
    }

    /**
     * 
     * @param result
     *     The result
     */
    public void setResult(Result result) {
        this.result = result;
    }

    public AccountTx withResult(Result result) {
        this.result = result;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(result).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AccountTx) == false) {
            return false;
        }
        AccountTx rhs = ((AccountTx) other);
        return new EqualsBuilder().append(result, rhs.result).isEquals();
    }

}
