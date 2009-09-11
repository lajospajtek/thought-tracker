using System;
using System.Collections.Generic;
using NUnit.Framework;

namespace ThoughtTracker.Blogspot.Com.Enumerating {
    [TestFixture]
    public class Specifications {
        private string[] _array;

        [SetUp]
        public void BeforeEachTest() {
            _array = new string[] { "1", "6", "5", "4", "2", "3", "not a number" };
        }


        [Test]
        public void should_find_existing_element() {
            Assert.AreEqual("2", Enumerating.On(_array).Find(delegate(string candidate) { return candidate == "2"; }));
        }


        [Test, ExpectedException(typeof(ArgumentException))]
        public void should_throw_searching_a_not_existing_element() {
            Enumerating.On(_array).Find((delegate(string candidate) { return candidate == "not in there"; }));
        }


        private bool IsEven(string number_representation) {
            int number;
            if (Int32.TryParse(number_representation, out number)) return number % 2 == 0;
            return false;
        }


        private void should_contain<T>(ICollection<T> collection, params T[] expected_items) {
            foreach (T expected_item in expected_items) Assert.IsTrue(collection.Contains(expected_item));
        }


        [Test]
        public void should_select_existing_elements() {
            ICollection<string> result = Enumerating.On(_array).Select(IsEven).ToCollection();
            Assert.AreEqual(3, result.Count);
            should_contain(result, "2", "4", "6");
        }


        [Test]
        public void selecting_non_existing_elements_should_deliver_an_empty_collection() {
            ICollection<string> result = Enumerating.On(_array).Select(delegate { return false; }).ToCollection();
            Assert.AreEqual(0, result.Count);
        }


        private int? ToIntOrNull(string candidate) {
            int number;
            if (Int32.TryParse(candidate, out number)) return number;
            return null;
        }


        [Test]
        public void should_collect_the_transformation_mapped_on_all_elements() {
            ICollection<int?> result = Enumerating.On(_array).Collect<int?>(ToIntOrNull).ToCollection();
            Assert.AreEqual(_array.Length, result.Count);
            should_contain<int?>(result, 1, 2, 3, 4, 5, 6, null);
        }


        [Test]
        public void should_compute_the_summ_of_elements() {
            int result = Enumerating.On(_array).Inject(0, delegate(int accumulator, string candidate) {
                int value = 0;
                Int32.TryParse(candidate, out value);
                return accumulator + value;
            });
            Assert.AreEqual(21, result);
        }


        [Test]
        public void should_detect_non_numbers() {
            bool result = Enumerating.On(_array).Exists(delegate(string candidate) {
                int value;
                return Int32.TryParse(candidate, out value);
            });
            Assert.IsTrue(result);
        }


        [Test]
        public void should_detect_all_non_empty_strings() {
            bool result =
                Enumerating.On(_array).AreAllSatisfying(delegate(string candidate) { return candidate.Length != 0; });
            Assert.IsTrue(result);
        }

        [Test]
        public void Can_Chain_Select_Collect_and_More() {
            bool satisfying = Enumerating.On(_array)
                .Select(IsEven)
                .Collect<string>(delegate(string input) { return input + "-" + input; })
                .AreAllSatisfying(Satisfies);
            Assert.IsTrue(satisfying);
        }


        private bool Satisfies(string obj) {
            return obj == "2-2" || obj == "4-4" || obj == "6-6";
        }
    }
}