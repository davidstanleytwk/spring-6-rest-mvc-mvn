package guru.springframework.spring6restmvc.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID",strategy = "org.hibernate.id.UUIDGenerator")
    // tell hibernate to store UUID as a CHAR so it's readable
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length=36,columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;
    private String customerName;
    @Column(length = 255)
    private String email;
    @Version
    private Integer version;
    @CreationTimestamp
    private LocalDateTime createdDate;
    @UpdateTimestamp
    private LocalDateTime lastUpdatedDate;

}
