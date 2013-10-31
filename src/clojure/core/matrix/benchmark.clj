(ns clojure.core.matrix.benchmark
  (:use clojure.core.matrix)
  (:use clojure.core.matrix.stats)
  (:require [clojure.core.matrix.operators])
  (:require [clojure.core.matrix.protocols :as mp])
  (:require [criterium.core :as c])
  (:require [mikera.vectorz.matrix-api])
  (:require [mikera.vectorz.core :as v])
  (:require [mikera.vectorz.matrix :as m])
  (:import [mikera.vectorz Vector3 Vectorz]))


(defn benchmarks []

  (set-current-implementation :vectorz)
  (set-current-implementation :clatrix)

;; benchmark for mutable vs immutable operations

  ;; mutable operation
  ;; 72ns
  (let [a (array (range 100))
        b (array (range 100))]
    (c/quick-bench
      (add! a b)))
  
  ;; immutable operation
  ;; 320ns
  (let [a (array (range 100))
        b (array (range 100))]
    (c/quick-bench
      (add a b)))  
  
  
  
)