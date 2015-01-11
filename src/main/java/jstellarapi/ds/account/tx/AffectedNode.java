
package jstellarapi.ds.account.tx;

import javax.annotation.Generated;
import javax.validation.Valid;
import com.google.gson.annotations.Expose;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class AffectedNode {

    @Expose
    @Valid
    private jstellarapi.ds.account.tx.CreatedNode CreatedNode;
    @Expose
    @Valid
    private jstellarapi.ds.account.tx.ModifiedNode ModifiedNode;

    /**
     * 
     * @return
     *     The CreatedNode
     */
    public jstellarapi.ds.account.tx.CreatedNode getCreatedNode() {
        return CreatedNode;
    }

    /**
     * 
     * @param CreatedNode
     *     The CreatedNode
     */
    public void setCreatedNode(jstellarapi.ds.account.tx.CreatedNode CreatedNode) {
        this.CreatedNode = CreatedNode;
    }

    public AffectedNode withCreatedNode(jstellarapi.ds.account.tx.CreatedNode CreatedNode) {
        this.CreatedNode = CreatedNode;
        return this;
    }

    /**
     * 
     * @return
     *     The ModifiedNode
     */
    public jstellarapi.ds.account.tx.ModifiedNode getModifiedNode() {
        return ModifiedNode;
    }

    /**
     * 
     * @param ModifiedNode
     *     The ModifiedNode
     */
    public void setModifiedNode(jstellarapi.ds.account.tx.ModifiedNode ModifiedNode) {
        this.ModifiedNode = ModifiedNode;
    }

    public AffectedNode withModifiedNode(jstellarapi.ds.account.tx.ModifiedNode ModifiedNode) {
        this.ModifiedNode = ModifiedNode;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(CreatedNode).append(ModifiedNode).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AffectedNode) == false) {
            return false;
        }
        AffectedNode rhs = ((AffectedNode) other);
        return new EqualsBuilder().append(CreatedNode, rhs.CreatedNode).append(ModifiedNode, rhs.ModifiedNode).isEquals();
    }

}
