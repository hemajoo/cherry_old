/*
 * Copyright(c) 2021 Hemajoo Digital Systems Inc.
 * --------------------------------------------------------------------------------------
 * This file is part of Hemajoo Systems Inc. projects which is licensed
 * under the Apache license version 2 and use is subject to license terms.
 * You should have received a copy of the license with the project's artifact
 * binaries and/or sources.
 *
 * License can be consulted at http://www.apache.org/licenses/LICENSE-2.0
 * --------------------------------------------------------------------------------------
 */
package com.hemajoo.commerce.cherry.persistence.model.entity.base;

import com.hemajoo.commerce.cherry.commons.type.StatusType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

/**
 * Represents the base status part of a persistence entity of the {@code Cherry} data model.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public abstract class StatusEntity extends AuditEntity
{
    public static final String FIELD_STATUS_TYPE    = "statusType";
    public static final String FIELD_SINCE          = "since";

    /**
     * Entity status.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS_TYPE", length = 50)
    private StatusType statusType;

    /**
     * Inactivity time stamp information (server time) that must be filled when the entity becomes inactive.
     */
    @ApiModelProperty(hidden = true)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SINCE", length = 26)
    private Date since;

    /**
     * Returns if this entity is active?
     * @return {@code True} if the entity is active, {@code false} otherwise.
     */
    @ApiModelProperty(hidden = true) // STATUS is used instead!
    public final boolean isActive()
    {
        return statusType == StatusType.ACTIVE;
    }

    /**
     * Sets the underlying entity to {@link StatusType#ACTIVE}.
     */
    public final void setActive()
    {
        statusType = StatusType.ACTIVE;
        since = null;
    }

    /**
     * Sets the underlying entity to {@link StatusType#INACTIVE}.
     */
    public final void setInactive()
    {
        statusType = StatusType.INACTIVE;
        since = new Date(System.currentTimeMillis());
    }

    /**
     * Sets the status of the entity.
     * @param status Status.
     * @see StatusType
     */
    public void setStatusType(StatusType status)
    {
        if (status != this.statusType)
        {
            if (status == StatusType.INACTIVE)
            {
                setInactive();
            }
            else
            {
                setActive();
            }
        }
    }
}
