package teamseven.echoeco.character.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import teamseven.echoeco.character.domain.CharacterType;
import teamseven.echoeco.character.domain.dto.CharacterResponse;
import teamseven.echoeco.character.domain.dto.QCharacterResponse;

import java.util.List;

import static teamseven.echoeco.character.domain.QCharacter.character;


@Repository
@RequiredArgsConstructor
public class CharacterCustomRepositoryImpl implements CharacterCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<CharacterResponse> searchPickList(Boolean isPossible) {
        return queryFactory
                .select(new QCharacterResponse(
                        character.id,
                        character.type,
                        character.name,
                        character.descriptions,
                        character.maxLevel,
                        character.isPossible
                ))
                .from(character)
                .where(
                        isPossibleEq(isPossible)
                ).limit(20L)
                .offset(0L)
                .fetch();
    }

    private BooleanExpression characterTypeEq(CharacterType type) {
        if (type == null || type.name().equals("ALL")) {
            return null;
        }
        return character.type.eq(type);
    }
    private BooleanExpression isPossibleEq(Boolean isPossible) {
        return isPossible != null ? character.isPossible.eq(isPossible) : null;
    }
}
