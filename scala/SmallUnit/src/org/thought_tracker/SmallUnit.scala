package org.thought_tracker;

object SmallUnit {
  
  case class AssertFailed(val msg: String) extends Throwable {
    override def toString() = "failed assertion: " + msg
  }

  abstract class Result 
  case object Success extends Result
  case class Failure(test: Test, throwable: Throwable) extends Result

  class ResultCollector(val results : Seq[Result]) {
      import java.io.StringWriter;
      import java.io.PrintWriter;

      lazy val failures = results filter is_failure
      
      private def is_failure(r : Result) : Boolean = {
          r match {
              case Failure(_,_) => true;
              case _ => false;
         }
      }
      
      private def toString(r : Result) : String = {
          r match {
              case Success => ".";
              case Failure(_,_) => "F";
          }
      }

      private def pp(funcs : (PrintWriter => Unit)*) : String = {
          val writer = new StringWriter
          val printer = new PrintWriter(writer, true)

          for ( f <- funcs) yield f(printer)
          
          writer.flush
          writer.toString
      }
      
      private def failureToString(r : Result) : String = {
          pp((p : PrintWriter) => {
              r match {
                  case Failure(test, throwable)  => {
                      p.println(">>> '" + test.toString() + "'" + " failed due to " + throwable.toString());
                      p.println(throwable.getStackTraceString)
                      p.println
                  }
              }
          })
      }
      
      private def print_runs(printer: PrintWriter) : Unit = {
          printer.println("Results: " + results.size) ;
          val x = for (r <- results) yield printer.print(toString(r))
          printer.println
      }
      
      private def print_failures(printer : PrintWriter) : Unit = {
          if (! failures.isEmpty) {
              printer.println("Failures:")
              for (f <- failures)  yield printer.println(failureToString(f))
          }
      }

      override def toString() : String = {
		  pp(print_runs, print_failures)     
      }
  }
  
  def should_be(func : => Boolean, msg: String) = {
  	    if(! func) fail(msg);
  }
  
  def should_not_be(func : => Boolean, msg: String) = {
        if(func) fail(msg);
}
  def fail(msg: String) = throw new AssertFailed(msg)
  
  
  def run1[T <% Test](test : T) : Result = {
    try { 
      test.run(); Success;
    } catch { 
      case t : Throwable => Failure(test, t);
    }
  }

  
  def run_tests[T <% Test](tests : T*) : ResultCollector = {
    val results = for (t <- tests) yield run1(t)
    new ResultCollector(results)
  }

  abstract class Test(val name : String) {
    def run() : Unit
    override def toString() : String = name
  }
  
  def test(name : String, func : => Unit) : Test = new Test(name) {
    def run = { func } 
  }

}

