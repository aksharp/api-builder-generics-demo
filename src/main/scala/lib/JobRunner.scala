package lib

import lib.generated.models.{JobError, JobInstance}

class JobRunner[J, I, O, E <: JobError] {
  def run(job: J, jobInput: I)
    (implicit runStrategy: RunStrategy[J, I, O, E]): Either[E, JobInstance[J, I, O, E]] = runStrategy(job, jobInput)
}

trait RunStrategy[J, I, O, E <: JobError] {
  def apply(job: J, input: I): Either[E, JobInstance[J, I, O, E]]
}