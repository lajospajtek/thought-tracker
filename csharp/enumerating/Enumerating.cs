using System.Collections.Generic;

namespace ThoughtTracker.Blogspot.Com.Enumerating {
    public class Tuple<T1, T2> {
            public T1 Field1;
            public T2 Field2;

            public Tuple(T1 field1, T2 field2) {
                Field1 = field1;
                Field2 = field2;
            }
        }

        public delegate void Action<A1, A2>(A1 arg1, A2 arg2);

        public delegate void Action();

        public delegate R Func<R>();

        public delegate R Func<R, A1>(A1 arg1);

        public delegate R Func<R, A1, A2>(A1 arg1, A2 arg2);

        public delegate R Func<R, A1, A2, A3>(A1 arg1, A2 arg2, A3 arg3);

        public delegate R Func<R, A1, A2, A3, A4>(A1 arg1, A2 arg2, A3 arg3, A4 arg4);


        public class Enumerating {
            public static IEnumerating<T> On<T>(IEnumerable<T> source) { return new EnumeratingOnOneSource<T>(source); }
            public  static IEnumeratingOnPairs<T1, T2> On<T1, T2>(IEnumerable<T1> source1, IEnumerable<T2> source2) {
                return new EnumeratingOnPairs<T1, T2>(source1, source2);
            }
        }
}
