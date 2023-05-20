package com.adem.elasticsearch.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.CreateResponse;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import com.adem.elasticsearch.dto.PageVO;
import com.adem.elasticsearch.dto.UserEsQueryVO;
import com.adem.elasticsearch.dto.UserVO;
import com.adem.elasticsearch.service.UserService;
import com.adem.elasticsearch.utility.EsJsonDataUtils;
import com.adem.elasticsearch.utility.StringUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final ElasticsearchClient esClient;
    private final static String USER_INDEX = "user";

    @Override
    public String createUser(UserVO userVO) throws IOException {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        userVO.setId(uuid);
        CreateResponse user = null;
        JsonData jd = EsJsonDataUtils.object2JsonData(userVO, esClient);
        try {
            user = esClient.create(cr -> cr.id("User" + "_" + userVO.getId())
                    .index("user")
                    .document(jd));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return user.result().name();
    }

    @Override
    public String deleteUser(String userId) throws IOException {
        DeleteResponse user = esClient.delete(del -> del.id("User" + "_" + userId).index("user"));
        return user.result().name();
    }

    @Override
    public String updateUser(UserVO userVO) throws IOException {
        BooleanResponse booleanResponse = esClient.existsSource(q -> q.index(USER_INDEX).id("User" + "_" + userVO.getId()));
        if (booleanResponse.value()) {
            try {
                JsonData jd = EsJsonDataUtils.object2JsonData(userVO, esClient);
                esClient.update(a -> a.index("user")
                        .id("User" + "_" + userVO.getId())
                        .doc(jd), jd.getClass());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        } else {
            throw new RuntimeException("User Not Exist");
        }
        return "Updated";
    }

    @Override
    public UserVO getUser(String userId) throws IOException {
        GetResponse<JsonData> user = esClient.get(a -> a.index("user")
                .id("User" + "_" + userId), JsonData.class);
        UserVO userVO = null;
        if (Objects.nonNull(user) && Objects.nonNull(user.source())) {
            userVO = user.source().to(UserVO.class);
        }
        return userVO;
    }

    public List<UserVO> queryUser(UserEsQueryVO queryVO, PageVO pageVO) throws IOException {
        List<Query> queries = generateQuery(queryVO);
        SearchResponse<UserVO> user;
        if (CollectionUtils.isNotEmpty(queries)) {
            user = esClient.search(s -> s
                            .index("user")
                            .query(q -> q
                                    .bool(b -> b
                                                    .should(queries)
                                            //.must(wildCardByName)
                                    )

                            )
                            .size(pageVO.getPageSize() - 1)
                            .from(pageVO.getPage() * pageVO.getPageSize()),
                    UserVO.class
            );
        } else {
            user = esClient.search(s -> s
                            .index("user")
                            .size(pageVO.getPageSize())
                            .from((pageVO.getPage() - 1) * pageVO.getPageSize()),
                    UserVO.class
            );
        }

        return user.hits().hits().stream().map(Hit::source).collect(Collectors.toList());
    }

    private List<Query> generateQuery(UserEsQueryVO queryVO) {
        List<Query> queries = new ArrayList<>();
        if (StringUtils.isNotEmpty(queryVO.getName())) {
            Query name = Query.of(q -> q.wildcard(w -> w.field("name")
                    .value(StringUtility.wildCard(queryVO.getName()))
                    .caseInsensitive(true)));
            queries.add(name);
        }

        if (StringUtils.isNotEmpty(queryVO.getLastName())) {
            Query lastName = Query.of(q -> q.wildcard(w -> w.field("name")
                    .value(StringUtility.wildCard(queryVO.getLastName()))
                    .caseInsensitive(true)));
            queries.add(lastName);
        }

        return queries;
    }
}
