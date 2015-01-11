
package jstellarapi.ds.account.tx;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Tx {

    @Expose
    private String Account;
    @Expose
    private Balance Amount;
    @Expose
    private String Destination;
    @Expose
    private String Fee;
    @Expose
    private long Flags;
    @Expose
    private long Sequence;
    @Expose
    private String SigningPubKey;
    @Expose
    private String TransactionType;
    @Expose
    private String TxnSignature;
    @Expose
    private long date;
    @Expose
    private String hash;
    @Expose
    private long inLedger;
    @SerializedName("ledger_index")
    @Expose
    private long ledgerIndex;

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

    public Tx withAccount(String Account) {
        this.Account = Account;
        return this;
    }

    /**
     * 
     * @return
     *     The Amount
     */
    public Balance getAmount() {
        return Amount;
    }

    /**
     * 
     * @param Amount
     *     The Amount
     */
    public void setAmount(Balance Amount) {
        this.Amount = Amount;
    }

    public Tx withAmount(Balance Amount) {
        this.Amount = Amount;
        return this;
    }

    /**
     * 
     * @return
     *     The Destination
     */
    public String getDestination() {
        return Destination;
    }

    /**
     * 
     * @param Destination
     *     The Destination
     */
    public void setDestination(String Destination) {
        this.Destination = Destination;
    }

    public Tx withDestination(String Destination) {
        this.Destination = Destination;
        return this;
    }

    /**
     * 
     * @return
     *     The Fee
     */
    public String getFee() {
        return Fee;
    }

    /**
     * 
     * @param Fee
     *     The Fee
     */
    public void setFee(String Fee) {
        this.Fee = Fee;
    }

    public Tx withFee(String Fee) {
        this.Fee = Fee;
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

    public Tx withFlags(long Flags) {
        this.Flags = Flags;
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

    public Tx withSequence(long Sequence) {
        this.Sequence = Sequence;
        return this;
    }

    /**
     * 
     * @return
     *     The SigningPubKey
     */
    public String getSigningPubKey() {
        return SigningPubKey;
    }

    /**
     * 
     * @param SigningPubKey
     *     The SigningPubKey
     */
    public void setSigningPubKey(String SigningPubKey) {
        this.SigningPubKey = SigningPubKey;
    }

    public Tx withSigningPubKey(String SigningPubKey) {
        this.SigningPubKey = SigningPubKey;
        return this;
    }

    /**
     * 
     * @return
     *     The TransactionType
     */
    public String getTransactionType() {
        return TransactionType;
    }

    /**
     * 
     * @param TransactionType
     *     The TransactionType
     */
    public void setTransactionType(String TransactionType) {
        this.TransactionType = TransactionType;
    }

    public Tx withTransactionType(String TransactionType) {
        this.TransactionType = TransactionType;
        return this;
    }

    /**
     * 
     * @return
     *     The TxnSignature
     */
    public String getTxnSignature() {
        return TxnSignature;
    }

    /**
     * 
     * @param TxnSignature
     *     The TxnSignature
     */
    public void setTxnSignature(String TxnSignature) {
        this.TxnSignature = TxnSignature;
    }

    public Tx withTxnSignature(String TxnSignature) {
        this.TxnSignature = TxnSignature;
        return this;
    }

    /**
     * 
     * @return
     *     The date
     */
    public long getDate() {
        return date;
    }

    /**
     * 
     * @param date
     *     The date
     */
    public void setDate(long date) {
        this.date = date;
    }

    public Tx withDate(long date) {
        this.date = date;
        return this;
    }

    /**
     * 
     * @return
     *     The hash
     */
    public String getHash() {
        return hash;
    }

    /**
     * 
     * @param hash
     *     The hash
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    public Tx withHash(String hash) {
        this.hash = hash;
        return this;
    }

    /**
     * 
     * @return
     *     The inLedger
     */
    public long getInLedger() {
        return inLedger;
    }

    /**
     * 
     * @param inLedger
     *     The inLedger
     */
    public void setInLedger(long inLedger) {
        this.inLedger = inLedger;
    }

    public Tx withInLedger(long inLedger) {
        this.inLedger = inLedger;
        return this;
    }

    /**
     * 
     * @return
     *     The ledgerIndex
     */
    public long getLedgerIndex() {
        return ledgerIndex;
    }

    /**
     * 
     * @param ledgerIndex
     *     The ledger_index
     */
    public void setLedgerIndex(long ledgerIndex) {
        this.ledgerIndex = ledgerIndex;
    }

    public Tx withLedgerIndex(long ledgerIndex) {
        this.ledgerIndex = ledgerIndex;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(Account).append(Amount).append(Destination).append(Fee).append(Flags).append(Sequence).append(SigningPubKey).append(TransactionType).append(TxnSignature).append(date).append(hash).append(inLedger).append(ledgerIndex).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Tx) == false) {
            return false;
        }
        Tx rhs = ((Tx) other);
        return new EqualsBuilder().append(Account, rhs.Account).append(Amount, rhs.Amount).append(Destination, rhs.Destination).append(Fee, rhs.Fee).append(Flags, rhs.Flags).append(Sequence, rhs.Sequence).append(SigningPubKey, rhs.SigningPubKey).append(TransactionType, rhs.TransactionType).append(TxnSignature, rhs.TxnSignature).append(date, rhs.date).append(hash, rhs.hash).append(inLedger, rhs.inLedger).append(ledgerIndex, rhs.ledgerIndex).isEquals();
    }

}
