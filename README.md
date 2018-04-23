# api-builder-generics-demo

If generics are adopted in API Builder & HackDao Code Generators, here's what Lib Job Usage would doc would look like:

**POSTGRES:** 
1) Run **job_instances.sql** to create job_instances table

**SCALA:**
1) Import lib in build.sbt
2) Include link to lib in API Builder Service API Spec & generate code
3) Define Job Run/Recover Strategies (Ex: job.strategies.*)
4) Run or Recover jobs (Ex: RunMain / RecoverMain)  
