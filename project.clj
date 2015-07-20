(defproject net.mikera/core.matrix.benchmark "0.0.1-SNAPSHOT"
  :description "Benchmarks for core.matrix implementations."
  :url "https://github.com/mikera/core.matrix.benchmark"
  :dev-dependencies [[lein-expectations "0.0.8"]
                     [expectations "1.4.41"]]
  :dependencies [[org.clojure/clojure "1.8.0-alpha2"]
                 [net.mikera/core.matrix "0.36.1"]
                 [net.mikera/core.matrix.stats "0.6.0"]
                 [net.mikera/vectorz-clj "0.30.1"]
                 [clatrix "0.5.0"]]
  :profiles {:dev {:dependencies [[criterium/criterium "0.4.3"]
                                  [com.taoensso/encore "2.1.0"]
                                  [expectations "2.1.2"]]}})
