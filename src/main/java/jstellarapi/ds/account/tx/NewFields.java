
package jstellarapi.ds.account.tx;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class NewFields {

    @Expose
    private String Account;
    @Expose
    private Balance Balance;
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

    public NewFields withAccount(String Account) {
        this.Account = Account;
        return this;
    }

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

    public NewFields withBalance(Balance Balance) {
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

    public NewFields withSequence(long Sequence) {
        this.Sequence = Sequence;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(Account).append(Balance).append(Sequence).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof NewFields) == false) {
            return false;
        }
        NewFields rhs = ((NewFields) other);
        return new EqualsBuilder().append(Account, rhs.Account).append(Balance, rhs.Balance).append(Sequence, rhs.Sequence).isEquals();
    }

}
