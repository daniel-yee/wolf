package study.daydayup.wolf.dts.source.offset;

import lombok.NonNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import study.daydayup.wolf.common.io.jdbc.JdbcMapper;
import study.daydayup.wolf.common.io.sql.Sql;
import study.daydayup.wolf.common.io.sql.SqlStatement;
import study.daydayup.wolf.common.util.collection.CollectionUtil;
import study.daydayup.wolf.common.util.lang.StringUtil;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * study.daydayup.wolf.framework.layer.dal.scanner
 *
 * @author Wingle
 * @since 2020/2/5 5:26 下午
 **/
@Component
public class MysqlOffsetHolder implements OffsetHolder {
    private static final String HOLDER_TABLE = "offset_holder";
    @Resource
    private JdbcTemplate jdbc;

    public Map<String, Long> getAll(@NonNull String source, @NonNull String table, @NonNull String shard) {
        String key = formatKey(source, table, shard);
        if (!OffsetLocker.lock(key)) {
            return null;
        }

        List<Map<String, Object>> data = getAllFromDb(source, table, shard);
        Map<String, Long> result = new HashMap<>();
        if (CollectionUtil.notEmpty(data)) {
            String tmpKey;
            for (Map<String, Object> row : data) {
                tmpKey = formatKey(source, table, shard, (String) row.get("sink"));
                result.put(tmpKey, (Long)row.get("offset"));
            }
        }

        OffsetLocker.unlock(key);
        return result;
    }

    @Override
    public Long get(@NonNull String source, @NonNull String table, @NonNull String shard, @NonNull String sink) {
        String key = formatKey(source, table, shard, sink);
        if (!OffsetLocker.lock(key)) {
            return null;
        }

        Long offset = getOffsetFromDb(source, table, shard, sink);

        OffsetLocker.unlock(key);
        return offset;
    }

    @Override
    public int set(@NonNull String source, @NonNull String table, @NonNull String shard, @NonNull String sink, @NonNull Long preOffset, @NonNull Long newOffset) {
        String key = formatKey(source, table, shard, sink);
        if (!OffsetLocker.lock(key)) {
            return 0;
        }

        int count = updateOffsetToDb(source, table, shard, sink, preOffset, newOffset);

        OffsetLocker.unlock(key);
        return count;
    }

    private int updateOffsetToDb(@NonNull String source, @NonNull String table, @NonNull String shard, @NonNull String sink, @NonNull Long preOffset, @NonNull Long newOffset) {
        Map<String, Object> data = new HashMap<>();
        data.put("offset", newOffset);
        data.put("version", SqlStatement.of(" version + 1 "));
        data.put("updated_at", LocalDateTime.now());

        Sql sql = Sql.update(HOLDER_TABLE)
                .set(data)
                .where(StringUtil.join( "source = '", source, "'"))
                .and(StringUtil.join( "sink = '", sink, "'"))
                .and(StringUtil.join( "table_name = '", table, "'"))
                .and(StringUtil.join( "sharding_key = '", shard, "'"))
                .and(StringUtil.join( "offset = ", preOffset , ""))
                .limit(1);
        return jdbc.update(sql.getSql(), sql.getData());
    }

    private void createOffset(@NonNull String source, @NonNull String table, @NonNull String shard, @NonNull String sink) {
        Map<String, Object> data = new HashMap<>();
        data.put("source", source);
        data.put("sink", sink);
        data.put("table_name", table);
        data.put("sharding_key", shard);
        data.put("offset", 0);
        data.put("created_at", LocalDateTime.now());

        Sql sql = Sql.insert(HOLDER_TABLE)
                .values(data);
        jdbc.update(sql.getSql(), sql.getData());
    }

    private Long getOffsetFromDb(@NonNull String source, @NonNull String table, @NonNull String shard, @NonNull String sink) {
        Sql sql = Sql.select("offset")
                .from(HOLDER_TABLE)
                .where(StringUtil.join( "source = '", source, "'"))
                .and(StringUtil.join( "sink = '", sink, "'"))
                .and(StringUtil.join( "table_name = '", table, "'"))
                .and(StringUtil.join( "sharding_key = '", shard, "'"))
                .limit(1);

        List<Long> offsetList = jdbc.queryForList(sql.getSql(), sql.getData(), Long.class);
        if (!CollectionUtil.notEmpty(offsetList)) {
            createOffset(source, table, shard, sink);
            return 0L;
        }

        return offsetList.get(0);
    }

    private List<Map<String, Object>> getAllFromDb(@NonNull String source, @NonNull String table, @NonNull String shard) {
        Sql sql = Sql.select("sink, offset")
                .from(HOLDER_TABLE)
                .where(StringUtil.join( "source = '", source, "'"))
                .and(StringUtil.join( "table_name = '", table, "'"))
                .and(StringUtil.join( "sharding_key = '", shard, "'"))
                ;

        List<Map<String, Object>> result = jdbc.queryForList(sql.getSql(), sql.getData());
        return JdbcMapper.map(result);
    }

}
