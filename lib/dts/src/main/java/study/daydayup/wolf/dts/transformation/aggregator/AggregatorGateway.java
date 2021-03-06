package study.daydayup.wolf.dts.transformation.aggregator;


import lombok.NonNull;
import study.daydayup.wolf.common.io.db.Row;
import study.daydayup.wolf.dts.transformation.Statistics;
import study.daydayup.wolf.common.io.db.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * study.daydayup.wolf.common.io.db
 *
 * @author Wingle
 * @since 2020/2/4 5:19 下午
 **/
public class AggregatorGateway extends AbstractAggregator implements Aggregator {
    private List<Aggregator> aggregatorList;

    public AggregatorGateway(Statistics statistics) {
        this.statistics = statistics;
        this.aggregatorList = new ArrayList<>(5);
    }

    @Override
    public void aggregate(Row row) {
        if (aggregatorList.isEmpty()) {
            return;
        }

        statistics.addRow(row);
        for (Aggregator aggregator : aggregatorList) {
            aggregator.aggregate(row);
        }
    }

    @Override
    public void merge(Row row) {
        if (aggregatorList.isEmpty()) {
            return;
        }

        for (Aggregator aggregator : aggregatorList) {
            aggregator.merge(row);
        }
    }

    @Override
    public void format() {
        if (aggregatorList.isEmpty()) {
            return;
        }

        for (Aggregator aggregator : aggregatorList) {
            aggregator.format();
        }
    }

    public AggregatorGateway minId(@NonNull String statisticsColumn) {
        MinAggregator aggregator = new MinAggregator();

        aggregator.init(statistics, Table.DEFAULT_ID_COLUMN, statisticsColumn);
        aggregatorList.add(aggregator);

        return this;
    }

    public AggregatorGateway maxId(@NonNull String statisticsColumn) {
        MaxAggregator aggregator = new MaxAggregator();

        aggregator.init(statistics, Table.DEFAULT_ID_COLUMN, statisticsColumn);
        aggregatorList.add(aggregator);

        return this;
    }

    public AggregatorGateway count(@NonNull String statisticsColumn) {
        return count(Table.DEFAULT_COUNT_COLUMN, statisticsColumn);
    }

    public AggregatorGateway count(@NonNull String rowColumn, @NonNull String statisticsColumn) {
        CountAggregator aggregator = new CountAggregator();

        aggregator.init(statistics, rowColumn, statisticsColumn);
        aggregatorList.add(aggregator);

        return this;
    }

    public AggregatorGateway distinctCount(@NonNull String rowColumn, @NonNull String statisticsColumn) {
        DistinctCountAggregator aggregator = new DistinctCountAggregator();

        aggregator.init(statistics, rowColumn, statisticsColumn);
        aggregatorList.add(aggregator);

        return this;
    }

    public AggregatorGateway mapCount(@NonNull String rowColumn, @NonNull Map<Object, String> columnMap) {
        MapCountAggregator aggregator = new MapCountAggregator();

        aggregator.init(statistics, rowColumn, columnMap);
        aggregatorList.add(aggregator);

        return this;
    }

    public AggregatorGateway mapSum(@NonNull String rowColumn, @NonNull Map<Object, String> columnMap) {
        MapSumAggregator aggregator = new MapSumAggregator();

        aggregator.init(statistics, rowColumn, columnMap);
        aggregatorList.add(aggregator);

        return this;
    }


    public AggregatorGateway min(@NonNull String rowColumn, @NonNull String statisticsColumn) {
        Aggregator aggregator = new MinAggregator();

        aggregator.init(statistics, rowColumn, statisticsColumn);
        aggregatorList.add(aggregator);

        return this;
    }

    public AggregatorGateway max(@NonNull String rowColumn, @NonNull String statisticsColumn) {
        Aggregator aggregator = new MaxAggregator();

        aggregator.init(statistics, rowColumn, statisticsColumn);
        aggregatorList.add(aggregator);

        return this;
    }

    public AggregatorGateway sum(@NonNull String rowColumn, @NonNull String statisticsColumn) {
        Aggregator aggregator = new SumAggregator();

        aggregator.init(statistics, rowColumn, statisticsColumn);
        aggregatorList.add(aggregator);

        return this;
    }

    public AggregatorGateway avg(@NonNull String rowColumn, @NonNull String statisticsColumn) {
        Aggregator aggregator = new AvgAggregator();

        aggregator.init(statistics, rowColumn, statisticsColumn);
        aggregatorList.add(aggregator);

        return this;
    }

    public AggregatorGateway addAggregator(@NonNull Aggregator aggregator) {
        aggregatorList.add(aggregator);

        return this;
    }

}
