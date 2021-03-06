package model;

import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;

import javax.lang.model.element.Element;
import javax.persistence.*;

@Entity
@Table(name ="bet")
public class Bet {

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private int id;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="user")
	private User user;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="[condition]") //condition is a reserved sql keyword, so we need to use []
	private Condition condition;

	@Column(name = "amount")
	private BigDecimal amount = new BigDecimal("0.00");

	public Bet(){

	}

	public Bet(User user, Condition condition, BigDecimal amount){
		this.user = user;
		this.condition = condition;
		this.amount = amount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
