
package jstellarapi.ds.tx;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class FinalFields {

    @Expose
    private String Account;
    @Expose
    private Amount Balance;
    @Expose
    private long Flags;
    @Expose
    private long OwnerCount;
    @Expose
    private long Sequence;

    /**
     * 
     * @return
     *     The Account
     */
    public String getAccount() {
        return Account;
    }

    /**
     * 
     * @param Account
     *     The Account
     */
    public void setAccount(String Account) {
        this.Account = Account;
    }

    public FinalFields withAccount(String Account) {
        this.Account = Account;
        return this;
    }

    /**
     * 
     * @return
     *     The Balance
     */
    public Amount getBalance() {
        return Balance;
    }

    /**
     * 
     * @param Balance
     *     The Balance
     */
    public void setBalance(Amount Balance) {
        this.Balance = Balance;
    }

    public FinalFields withBalance(Amount Balance) {
        this.Balance = Balance;
        return this;
    }

    /**
     * 
     * @return
     *     The Flags
     */
    public long getFlags() {
        return Flags;
    }

    /**
     * 
     * @param Flags
     *     The Flags
     */
    public void setFlags(long Flags) {
        this.Flags = Flags;
    }

    public FinalFields withFlags(long Flags) {
        this.Flags = Flags;
        return this;
    }

    /**
     * 
     * @return
     *     The OwnerCount
     */
    public long getOwnerCount() {
        return OwnerCount;
    }

    /**
     * 
     * @param OwnerCount
     *     The OwnerCount
     */
    public void setOwnerCount(long OwnerCount) {
        this.OwnerCount = OwnerCount;
    }

    public FinalFields withOwnerCount(long OwnerCount) {
        this.OwnerCount = OwnerCount;
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

    public FinalFields withSequence(long Sequence) {
        this.Sequence = Sequence;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(Account).append(Balance).append(Flags).append(OwnerCount).append(Sequence).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof FinalFields) == false) {
            return false;
        }
        FinalFields rhs = ((FinalFields) other);
        return new EqualsBuilder().append(Account, rhs.Account).append(Balance, rhs.Balance).append(Flags, rhs.Flags).append(OwnerCount, rhs.OwnerCount).append(Sequence, rhs.Sequence).isEquals();
    }

}
