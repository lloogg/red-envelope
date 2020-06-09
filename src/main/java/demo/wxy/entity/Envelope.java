package demo.wxy.entity;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

/**
 * @author gnayuxiew
 */
@Entity
@Getter
@Setter
public class Envelope {
    /**
     * 大红包id
     */
    @Id
    @GeneratedValue
    private Integer envelopeId;
    /**
     * 大红包总金额
     */
    private Integer envelopeSum;
    /**
     * 大红包个数
     */
    private Integer envelopeCount;

    /**
     * 包含的小红包
     * 一对多
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, mappedBy = "packetEnvelope")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Packet> envelopePackets;


    /**
     * 这个红包谁发的
     * 多对一
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
