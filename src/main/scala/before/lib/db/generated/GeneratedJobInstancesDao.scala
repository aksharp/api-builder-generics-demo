package before.lib.db.generated

import java.sql.Connection

import anorm._
import io.flow.common.v0.models.UserReference
import io.flow.play.util.IdGenerator
import io.flow.postgresql.play.db.DbHelpers
import io.flow.postgresql.{OrderBy, Query}
import javax.inject.{Inject, Singleton}
import org.joda.time.DateTime
import play.api.db.{Database, NamedDatabase}
import play.api.libs.json.{JsObject, JsValue, Json}

case class JobInstance(
  id: String,
  key: String,
  job: JsObject,
  input: Option[JsObject],
  output: Option[JsObject],
  errors: Option[List[JsObject]],
  createdAt: DateTime,
  updatedAt: DateTime
) {

  lazy val form: JobInstanceForm = JobInstanceForm(
    key = key,
    job = job,
    input = input,
    output = output,
    errors = errors
  )

}

case class JobInstanceForm(
  key: String,
  job: JsValue,
  input: Option[JsValue],
  output: Option[JsValue],
  errors: Option[List[JsValue]]
) {
  assert(
    job.isInstanceOf[JsObject],
    s"Field[job] must be a JsObject and not a ${job.getClass.getName}"
  )

  assert(
    input.forall(_.isInstanceOf[JsObject]),
    s"Field[input] must be a JsObject and not a ${input.map(_.getClass.getName).getOrElse("Unknown")}"
  )

  assert(
    output.forall(_.isInstanceOf[JsObject]),
    s"Field[output] must be a JsObject and not a ${output.map(_.getClass.getName).getOrElse("Unknown")}"
  )

  assert(
    errors.getOrElse(Nil).forall(_.isInstanceOf[JsObject]),
    s"Field[errors] must contain JsObjects and not a ${errors.getOrElse(Nil).filterNot(_.isInstanceOf[JsObject]).map(_.getClass.getName).distinct}"
  )
}

@Singleton
class JobInstancesDao @Inject() (
  @NamedDatabase("default") db: Database
) {

  private[this] val idGenerator = IdGenerator("job")

  private[this] val dbHelpers = DbHelpers(db, "job_instances")

  private[this] val BaseQuery = Query("""
      | select job_instances.id,
      |        job_instances.key,
      |        job_instances.job::text as job_text,
      |        job_instances.input::text as input_text,
      |        job_instances.output::text as output_text,
      |        job_instances.errors::text as errors_text,
      |        job_instances.created_at,
      |        job_instances.updated_at,
      |        job_instances.updated_by_user_id,
      |        job_instances.hash_code
      |   from job_instances
  """.stripMargin)

  private[this] val InsertQuery = Query("""
    | insert into job_instances
    | (id, key, job, input, output, errors, updated_by_user_id, hash_code)
    | values
    | ({id}, {key}, {job}::json, {input}::json, {output}::json, {errors}::json, {updated_by_user_id}, {hash_code}::bigint)
  """.stripMargin)

  private[this] val UpdateQuery = Query("""
    | update job_instances
    |    set key = {key},
    |        job = {job}::json,
    |        input = {input}::json,
    |        output = {output}::json,
    |        errors = {errors}::json,
    |        updated_by_user_id = {updated_by_user_id},
    |        hash_code = {hash_code}::bigint
    |  where id = {id}
    |    and job_instances.hash_code != {hash_code}::bigint
  """.stripMargin)

  private[this] def bindQuery(query: Query, form: JobInstanceForm): Query = {
    query.
      bind("key", form.key).
      bind("job", form.job).
      bind("input", form.input).
      bind("output", form.output).
      bind("errors", form.errors.map { v => Json.toJson(v) }).
      bind("hash_code", form.hashCode())
  }

  def insert(updatedBy: UserReference, form: JobInstanceForm): String = {
    db.withConnection { implicit c =>
      insertWithConnection(c, updatedBy, form)
    }
  }

  def insertWithConnection(implicit c: Connection, updatedBy: UserReference, form: JobInstanceForm): String = {
    val id = idGenerator.randomId()
    bindQuery(InsertQuery, form).
      bind("id", id).
      bind("updated_by_user_id", updatedBy.id).
      anormSql.execute()
    id
  }

  def updateIfChangedById(updatedBy: UserReference, id: String, form: JobInstanceForm) {
    if (!findById(id).map(_.form).contains(form)) {
      updateById(updatedBy, id, form)
    }
  }

  def updateById(updatedBy: UserReference, id: String, form: JobInstanceForm) {
    db.withConnection { implicit c =>
      updateById(c, updatedBy, id, form)
    }
  }

  def updateById(implicit c: Connection, updatedBy: UserReference, id: String, form: JobInstanceForm) {
    bindQuery(UpdateQuery, form).
      bind("id", id).
      bind("updated_by_user_id", updatedBy.id).
      anormSql.execute()
  }

  def update(updatedBy: UserReference, existing: JobInstance, form: JobInstanceForm) {
    db.withConnection { implicit c =>
      update(c, updatedBy, existing, form)
    }
  }

  def update(implicit c: Connection, updatedBy: UserReference, existing: JobInstance, form: JobInstanceForm) {
    updateById(c, updatedBy, existing.id, form)
  }

  def delete(deletedBy: UserReference, jobInstance: JobInstance) {
    dbHelpers.delete(deletedBy, jobInstance.id)
  }

  def deleteById(deletedBy: UserReference, id: String) {
    db.withConnection { implicit c =>
      deleteById(c, deletedBy, id)
    }
  }

  def deleteById(c: java.sql.Connection, deletedBy: UserReference, id: String) {
    dbHelpers.delete(c, deletedBy, id)
  }

  def findById(id: String): Option[JobInstance] = {
    db.withConnection { implicit c =>
      findByIdWithConnection(c, id)
    }
  }

  def findByIdWithConnection(c: java.sql.Connection, id: String): Option[JobInstance] = {
    findAllWithConnection(c, ids = Some(Seq(id)), limit = 1).headOption
  }

  def findAll(
    ids: Option[Seq[String]] = None,
    job: Option[JsObject] = None,
    limit: Long,
    offset: Long = 0,
    orderBy: OrderBy = OrderBy("job_instances.id")
  ) (
    implicit customQueryModifier: Query => Query = { q => q }
  ): Seq[JobInstance] = {
    db.withConnection { implicit c =>
      findAllWithConnection(
        c,
        ids = ids,
        job = job,
        limit = limit,
        offset = offset,
        orderBy = orderBy
      )(customQueryModifier)
    }
  }

  def findAllWithConnection(
    c: java.sql.Connection,
    ids: Option[Seq[String]] = None,
    job: Option[JsObject] = None,
    limit: Long,
    offset: Long = 0,
    orderBy: OrderBy = OrderBy("job_instances.id")
  ) (
    implicit customQueryModifier: Query => Query = { q => q }
  ): Seq[JobInstance] = {
    customQueryModifier(BaseQuery).
      optionalIn("job_instances.id", ids).
      equals("job_instances.job", job).
      limit(limit).
      offset(offset).
      orderBy(orderBy.sql).
      as(JobInstancesDao.parser().*)(c)
  }

}

object JobInstancesDao {

  def parser(): RowParser[JobInstance] = {
    SqlParser.str("id") ~
    SqlParser.str("key") ~
    SqlParser.str("job_text") ~
    SqlParser.str("input_text").? ~
    SqlParser.str("output_text").? ~
    SqlParser.str("errors_text").? ~
    SqlParser.get[DateTime]("created_at") ~
    SqlParser.get[DateTime]("updated_at") map {
      case id ~ key ~ job ~ input ~ output ~ errors ~ createdAt ~ updatedAt => JobInstance(
        id = id,
        key = key,
        job = Json.parse(job).as[JsObject],
        input = input.map { text => Json.parse(text).as[JsObject] },
        output = output.map { text => Json.parse(text).as[JsObject] },
        errors = errors.map { text => Json.parse(text).as[List[JsObject]] },
        createdAt = createdAt,
        updatedAt = updatedAt
      )
    }
  }

}