(ns clojure.core.matrix.benchmark
  (:use clojure.repl)
  (:use clojure.core.matrix)
  (:use clojure.core.matrix.stats)
  (:require [clojure.core.matrix.operators])
  (:require [clojure.core.matrix.protocols :as mp])
  (:require [criterium.core :as c])
  (:require [mikera.vectorz.matrix-api])
  (:require [mikera.vectorz.core :as v])
  (:require [mikera.vectorz.matrix :as m])
  (:import [mikera.vectorz Vector3 Vectorz]))

(defmacro bench [exp]
  `(let [res# (c/quick-benchmark ~exp nil)]
     (* 1000000000 (first (:mean res#)))))

(def sizes (vec (concat [1 2 3 4 5 6 7 8 10 13] (map #(long (* 16 (pow 2.0 (/ % 3)))) (range 22))))) 
(def pot-sizes (vec (map #(long (pow 2.0 %)) (range 21)))) 

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
  
 (defn matrix-bench [impl sizes]
   (doseq [n sizes]
      (let [m (zero-matrix impl n n)]
        (println (str n \tab (bench (mmul m m)))))))
 
 (matrix-bench :vectorz sizes)
 (matrix-bench :clatrix sizes)
  
 (defn vector-bench [impl sizes]
   (doseq [n sizes]
      (let [a (zero-vector impl n)
           b (zero-vector impl n)]
        (assign! a (vec (range n)))
       (assign! b (vec (range n)))
        (println (str n \tab (bench (add a b)))))))

 (vector-bench :vectorz pot-sizes)
 (vector-bench :clatrix pot-sizes)
  
)