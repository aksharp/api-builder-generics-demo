package service

import lib.generated._
import service.generated._

object Implementations {

  implicit object MyDailyEtlJobRunStrategy extends (
    MyDailyEtlJob.type => Day => Either[JobError, TotalDailyRevenueByOrganization]
    ) {
    override def apply(job: MyDailyEtlJob.type): Day => Either[JobError, TotalDailyRevenueByOrganization] = {
      // dummy implementation (switch for real one)
      job => Right(TotalDailyRevenueByOrganization("flow", 1234567.89))
    }
  }

}
