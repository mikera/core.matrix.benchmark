(defproject net.mikera/core.matrix.benchmark "0.0.1-SNAPSHOT"
  :description "Benchmarks for core.matrix implementations."
  :url "https://github.com/mikera/core.matrix.benchmark"
  :dev-dependencies [[lein-expectations "0.0.8"]
                     [expectations "1.4.41"]]
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [net.mikera/core.matrix "0.13.0"]
                 [net.mikera/vectorz-clj "0.15.0"]
                 [clatrix "0.4.0-SNAPSHOT"]]
  :profiles {:dev {:dependencies [[criterium/criterium "0.4.1"]
                                  [expectations "1.4.41"]]}})
