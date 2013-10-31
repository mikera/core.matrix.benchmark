(ns clojure.core.matrix.benchmark.test-implementations
  (:use clojure.core.matrix)
  (:use clojure.core.matrix.stats)
  (:require [clojure.core.matrix.operators])
  (:require clojure.core.matrix.compliance-tester)
  (:require [clojure.core.matrix.protocols :as mp])
  (:require [criterium.core :as c]))


(deftest test-impls
  (clojure.core.matrix.compliance-tester/implementation-test :vectorz))