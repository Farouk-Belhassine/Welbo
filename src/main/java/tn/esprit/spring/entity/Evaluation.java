package tn.esprit.spring.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Evaluation implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String description;
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar evaldate;
	private String email;
	@Enumerated (EnumType.STRING)
	private ClassType classes;
	@JsonIgnore
	@ManyToMany(cascade= CascadeType.ALL)
	private List<User> evalUser;
	}