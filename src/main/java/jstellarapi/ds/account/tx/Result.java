
package jstellarapi.ds.account.tx;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.validation.Valid;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Result {

    @Expose
    private String account;
    @SerializedName("ledger_index_max")
    @Expose
    private long ledgerIndexMax;
    @SerializedName("ledger_index_min")
    @Expose
    private long ledgerIndexMin;
    @Expose
    private String status;
    @Expose
    @Valid
    private List<Transaction> transactions = new ArrayList<Transaction>();

    /**
     * 
     * @return
     *     The account
     */
    public String getAccount() {
        return account;
    }

    /**
     * 
     * @param account
     *     The account
     */
    public void setAccount(String account) {
        this.account = account;
    }

    public Result withAccount(String account) {
        this.account = account;
        return this;
    }

    /**
     * 
     * @return
     *     The ledgerIndexMax
     */
    public long getLedgerIndexMax() {
        return ledgerIndexMax;
    }

    /**
     * 
     * @param ledgerIndexMax
     *     The ledger_index_max
     */
    public void setLedgerIndexMax(long ledgerIndexMax) {
        this.ledgerIndexMax = ledgerIndexMax;
    }

    public Result withLedgerIndexMax(long ledgerIndexMax) {
        this.ledgerIndexMax = ledgerIndexMax;
        return this;
    }

    /**
     * 
     * @return
     *     The ledgerIndexMin
     */
    public long getLedgerIndexMin() {
        return ledgerIndexMin;
    }

    /**
     * 
     * @param ledgerIndexMin
     *     The ledger_index_min
     */
    public void setLedgerIndexMin(long ledgerIndexMin) {
        this.ledgerIndexMin = ledgerIndexMin;
    }

    public Result withLedgerIndexMin(long ledgerIndexMin) {
        this.ledgerIndexMin = ledgerIndexMin;
        return this;
    }

    /**
     * 
     * @return
     *     The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public Result withStatus(String status) {
        this.status = status;
        return this;
    }

    /**
     * 
     * @return
     *     The transactions
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * 
     * @param transactions
     *     The transactions
     */
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Result withTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(account).append(ledgerIndexMax).append(ledgerIndexMin).append(status).append(transactions).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Result) == false) {
            return false;
        }
        Result rhs = ((Result) other);
        return new EqualsBuilder().append(account, rhs.account).append(ledgerIndexMax, rhs.ledgerIndexMax).append(ledgerIndexMin, rhs.ledgerIndexMin).append(status, rhs.status).append(transactions, rhs.transactions).isEquals();
    }

}
