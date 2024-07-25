package teamseven.echoeco.gifticon.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import teamseven.echoeco.gifticon.domain.dto.GifticonUserAdminResponse;
import teamseven.echoeco.gifticon.domain.dto.QGifticonUserAdminResponse;

import java.util.List;

import static teamseven.echoeco.gifticon.domain.QGifticonUser.gifticonUser;


@Repository
@RequiredArgsConstructor
public class GifticonCustomRepositoryImpl implements GifticonCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<GifticonUserAdminResponse> search(String email, Boolean isSend) {
        return queryFactory
                .select(new QGifticonUserAdminResponse(
                        gifticonUser.id,
                        gifticonUser.user.email,
                        gifticonUser.name,
                        gifticonUser.isSend
                ))
                .from(gifticonUser)
                .where(
                        emailEq(email),
                        isSendEq(isSend)
                ).limit(100L)
                .offset(0L)
                .fetch();
    }

    private BooleanExpression isSendEq(Boolean isSend) {
        return isSend != null ? gifticonUser.isSend.eq(isSend) : null;
    }
    private BooleanExpression emailEq(String email) {
        return email != null ? gifticonUser.user.email.eq(email) : null;
    }
}
