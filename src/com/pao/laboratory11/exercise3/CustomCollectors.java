package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collector;

public final class CustomCollectors {

    public static Collector<Transaction, ?, Snapshot> toSnapshot(int topN) {

        class Agg {
            Map<String, Long> byCountry = new HashMap<>();
            Map<String, Long> byChannel = new HashMap<>();
            BigDecimal total = BigDecimal.ZERO;
            List<Transaction> all = new ArrayList<>();
        }

        return Collector.of(
                Agg::new,

                (agg, tx) -> {
                    agg.byCountry.merge(tx.getCountry(), 1L, Long::sum);
                    agg.byChannel.merge(tx.getChannel(), 1L, Long::sum);
                    agg.total = agg.total.add(tx.getAmount());
                    agg.all.add(tx);
                },

                (a, b) -> {
                    b.byCountry.forEach((k, v) -> a.byCountry.merge(k, v, Long::sum));
                    b.byChannel.forEach((k, v) -> a.byChannel.merge(k, v, Long::sum));
                    a.total = a.total.add(b.total);
                    a.all.addAll(b.all);
                    return a;
                },

                agg -> {
                    List<Transaction> top = agg.all.stream()
                            .sorted(Comparator.comparing(Transaction::getAmount).reversed())
                            .limit(topN)
                            .toList();

                    return new Snapshot(
                            new HashMap<>(agg.byCountry),
                            new HashMap<>(agg.byChannel),
                            agg.total,
                            top
                    );
                }
        );
    }
}
