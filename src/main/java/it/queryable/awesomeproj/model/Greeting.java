package it.queryable.awesomeproj.model;

import it.ness.queryable.annotations.*;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.persistence.*;
import static it.queryable.awesomeproj.management.AppConstants.GREETING_PATH;
import it.queryable.awesomeproj.model.enums.GreetingEnum;
import java.time.LocalDateTime;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@Entity
@Table(name = "greeetings")
@QRs(GREETING_PATH)
@QOrderBy("name asc")
@FilterDef(name = "Greeting.like.tagses", parameters = @ParamDef(name = "tagses", type = "string"))
@Filter(name = "Greeting.like.tagses", condition = "lower(tagses) LIKE :tagses")
@FilterDef(name = "Greeting.obj.uuid", parameters = @ParamDef(name = "uuid", type = "string"))
@Filter(name = "Greeting.obj.uuid", condition = "uuid = :uuid")
@FilterDef(name = "Greeting.obj.uuids", parameters = @ParamDef(name = "uuids", type = "string"))
@Filter(name = "Greeting.obj.uuids", condition = "uuid IN (:uuids)")
@FilterDef(name = "Greeting.like.name", parameters = @ParamDef(name = "name", type = "string"))
@Filter(name = "Greeting.like.name", condition = "lower(name) LIKE :name")
@FilterDef(name = "Greeting.obj.date_time", parameters = @ParamDef(name = "date_time", type = "LocalDateTime"))
@Filter(name = "Greeting.obj.date_time", condition = "date_time = :date_time")
@FilterDef(name = "Greeting.from.date_time", parameters = @ParamDef(name = "date_time", type = "LocalDateTime"))
@Filter(name = "Greeting.from.date_time", condition = "date_time >= :date_time")
@FilterDef(name = "Greeting.to.date_time", parameters = @ParamDef(name = "date_time", type = "LocalDateTime"))
@Filter(name = "Greeting.to.date_time", condition = "date_time <= :date_time")
@FilterDef(name = "Greeting.obj.greetingEnum", parameters = @ParamDef(name = "greetingEnum", type = "string"))
@Filter(name = "Greeting.obj.greetingEnum", condition = "greetingEnum = :greetingEnum")
@FilterDef(name = "Greeting.obj.active", parameters = @ParamDef(name = "active", type = "boolean"))
@Filter(name = "Greeting.obj.active", condition = "active = :active")
public class Greeting extends PanacheEntityBase {

	@Id
	@Q
	@QList
	public String uuid;

	@QLike
	public String name;

	@Q
	public LocalDateTime date_time;

	@Q
	@Enumerated(EnumType.STRING)
	public GreetingEnum greetingEnum;

	@QLikeList
	public String tags;

	@QLogicalDelete
	boolean active;
}