package demo.wxy.entity;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

/**
 * @author gnayuxiew
 */
@Getter
@Setter
@Entity
public class User {
    /**
     * 用户id，系统初始化时初始化1000个用户，id设为随机的uuid值
     */
    @Id
    @GeneratedValue
    private Integer userId;
    /**
     * 用户发出的大红包
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, mappedBy = "user")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Envelope> userSend;

    /**
     * 用户收到的红包
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, mappedBy = "packetUser")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Packet> userReceived;


    /**
     * 账户余额，系统初始化时设为100
     */
    private Double balance;
}
