package lib

import lib.generated.JobError

class JobRunner[J, I, O, E <: JobError] {
  def run(job: J, jobInput: I)
    (implicit runStrategy: J => I => Either[E, O]): Either[E, O] = runStrategy(job)(jobInput)
}