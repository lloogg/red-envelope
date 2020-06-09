package demo.wxy.entity;

import lombok.*;

import javax.persistence.*;

/**
 * @author gnayuxiew
 */
@Entity
@Getter
@Setter
public class Packet {
    /**
     * 小红包id
     */
    @Id
    @GeneratedValue
    private Integer packetId;

    /**
     * 小红包金额
     */
    private Double packetValue;

    /**
     * 抢到小红包的用户
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User packetUser;


    /**
     * 多对一
     */
    @ManyToOne
    @JoinColumn(name = "envelope_id")
    private Envelope packetEnvelope;
}
