(ns clojure.core.matrix.benchmark
  (:use clojure.repl)
  ;; Don't like :refer :all ; should swap out, but for now to get running
  (:require [clojure.core.matrix :as mat :refer :all])
  (:use clojure.core.matrix.stats)
  (:require [clojure.core.matrix.operators])
  (:require [clojure.core.matrix.protocols :as mp])
  (:require [criterium.core :as c])
  (:require [mikera.vectorz.matrix-api])
  (:require [mikera.vectorz.core :as v])
  (:require [mikera.vectorz.matrix :as m])
  (:import [mikera.vectorz Vector3 Vectorz]))


;; Some benchmarking macros

(defmacro bench-expr [exp]
  "Simply benchmark an expression and return the mean in secs"
  `(let [res# (c/quick-benchmark ~exp nil)]
     (first (:mean res#))))

(defn m-op-bench [m operation arity]
  "Takes a matrix, an operation fn, and an arity of the function, and benchmarks
  `(operation m m m...)` with m inserted `arity` times."
  (case arity
    1 (bench-expr (operation m))
    2 (bench-expr (operation m m))))


;; Matrix and vector generators

(defn setup-rand-matrix
  ([impl generator n m]
   (reduce
     (fn [mat [i j]] (mat/mset mat i j (generator)))
     (mat/new-matrix impl n m)
     (for [i (range n)
           j (range m)]
       [i j])))
  ([impl generator n]
   (setup-rand-matrix impl generator n n)))

(defn setup-rand-vector
  [impl generator n]
  (reduce
    (fn [v i] (mat/mset v i (generator)))
    (mat/new-vector impl n)
    (range n)))

;; A default/simple int generator...
(def int-gen (partial rand-int 10))


;; ## Benchmarks
;;
;; Here we construct a catalog of various benchmarks, which then become available in the functions below

(def benchmarks
  {:mmul          {:fn      mat/mmul
                   :arity   2}
   :mul           {:fn      mat/mul
                   :arity   2}
   :mul!          {:fn      mat/mul!
                   :arity   2
                   :mutable true}
   :inverse       {:fn      mat/inverse
                   :arity   1}
   :matrix/add    {:fn      mat/add
                   :arity   2}
   :matrix/add!   {:fn      mat/add!
                   :arity   2
                   :mutable true}
   ;; Vector benchmarks
   :normalise!    {:fn      mat/normalise!
                   :arity   1
                   :mutable true
                   :types   :vector}
   :normalise     {:fn      mat/normalise
                   :arity   1
                   :types   :vector}
   :inner-product {:fn      mat/inner-product
                   :arity   2
                   :types   :vector}
   :vector/add    {:fn      mat/add
                   :arity   2
                   :types   :vector}
   :vector/add!   {:fn      mat/add!
                   :arity   2
                   :types   :vector
                   :mutable true}})


(defn bench
  "Runs a benchmark for the given operation, given an implementation and a dimension"
  [operation-key impl dimension]
  (let [{f :fn arity :arity types :types :as op}
             (get benchmarks operation-key)
        morv (if (= :vector types)
               (setup-rand-vector impl int-gen dimension)
               (setup-rand-matrix impl int-gen dimension))
        ;; Hmm... letting the data type be mutable makes this take a lot longer. But then leaving it out, the
        ;; operation still works and is much faster. What's going on here?
        ;morv (if (:mutable op)
               ;(mat/mutable morv)
               ;morv)]
               ]
    (m-op-bench morv f arity)))


(defn benches
  "For each of the operations, impls, dimensions, does the given benchmark, and returns a map with
  keys `[:operation :implementation :dimension :bench]`. Note this function is lazy, so it's recommended
  you use `doall` if you want things computed greedily..."
  [operations impls dimensions]
  (for [operation operations
        impl      impls
        dimension dimensions]
    {:operation      operation
     :implementation impl
     :dimension      dimension
     :bench          (bench operation impl dimension)}))


(defn- row-printer
  [row]
  (println (clojure.string/join \tab row)))


(defn report-benches
  "Like benches, but prints output to stdout"
  [benches]
  (row-printer ["Operation" "Implementation" "Dimension" "Time (ns)"])
  (doseq [row benches]
    (row-printer (map row [:operation :implementation :dimension :bench]))))


(def sizes (vec (concat [1 2 3 4 5 6 7 8 10 13] (map #(long (* 16 (pow 2.0 (/ % 3)))) (range 22))))) 
(def pot-sizes (mapv #(long (pow 2.0 %)) (range 21)))


(defn benchmarks []
  ;; benchmark for mutable vs immutable operations
  ;; mutable operation
  ;; 72ns
  (bench :vector/add! :clatrix 100)
  (bench :vector/add! :vectorz 100)

   ;; immutable operation
  ;; 320ns
  (bench :vector/add :clatrix 100)
  (bench :vector/add :vectorz 100)

  (doseq [b (benches [:mmul] [:vectorz :clatrix] sizes)]
    (println (str "mmul size" (:dimension b) ":" \tab (:bench b))))

  (doseq [b (benches [:vector/add] [:vectorz :clatrix] pot-sizes)]
    (println (str "vector/add size" (:dimension b) ":" \tab (:bench b)))))


