package org.thought_tracker;

object main {
  import org.thought_tracker.SmallUnit._;
  
  def main(args : Array[String]) : Unit = {

      val ok = new Test("should be ok") {
        def run = Console.println("test doing nothing")
      }
      
      val runtime_poof = test("runtime poof", { throw new RuntimeException("m3m3") })
      
      //run_tests (a, ok, c);
      
      
      Console.println(run_tests(
      	test("should not display failures", {   
          val result = run_tests(ok)
          should_be(result.failures.isEmpty, "no failures")
          should_not_be(result.toString().contains("Failures"), "no failures reported")    
        }),
        
        test("should diplay failure", {
        	val result = run_tests(runtime_poof)
            should_not_be(result.failures.isEmpty, "there are failures")
            should_be(result.toString().contains("Failures"), "there are failures reported")
        }),
        
        test("should run both tests, continuing after error", {
            val result = run_tests(runtime_poof, ok)
            should_be(result.results.size == 2, "2 tests have run")
        })
      ))

  }
}
