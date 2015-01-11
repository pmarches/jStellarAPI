
package jstellarapi.ds.account.tx;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class PreviousFields {

    @Expose
    private Balance Balance;
    @Expose
    private long Sequence;

    /**
     * 
     * @return
     *     The Balance
     */
    public Balance getBalance() {
        return Balance;
    }

    /**
     * 
     * @param Balance
     *     The Balance
     */
    public void setBalance(Balance Balance) {
        this.Balance = Balance;
    }

    public PreviousFields withBalance(Balance Balance) {
        this.Balance = Balance;
        return this;
    }

    /**
     * 
     * @return
     *     The Sequence
     */
    public long getSequence() {
        return Sequence;
    }

    /**
     * 
     * @param Sequence
     *     The Sequence
     */
    public void setSequence(long Sequence) {
        this.Sequence = Sequence;
    }

    public PreviousFields withSequence(long Sequence) {
        this.Sequence = Sequence;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(Balance).append(Sequence).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PreviousFields) == false) {
            return false;
        }
        PreviousFields rhs = ((PreviousFields) other);
        return new EqualsBuilder().append(Balance, rhs.Balance).append(Sequence, rhs.Sequence).isEquals();
    }

}
