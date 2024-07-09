package teamseven.echoeco.admin.character.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import teamseven.echoeco.admin.character.domain.CharacterType;
import teamseven.echoeco.admin.character.domain.dto.CharacterPickListCondition;
import teamseven.echoeco.admin.character.domain.dto.CharacterResponse;
import teamseven.echoeco.admin.character.domain.dto.QCharacterResponse;

import java.util.List;

import static teamseven.echoeco.admin.character.domain.QCharacter.character;

@Repository
@RequiredArgsConstructor
public class CharacterCustomRepositoryImpl implements CharacterCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<CharacterResponse> searchPickList(CharacterType type, Boolean isPossible) {
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
                        characterTypeEq(type),
                        isPossibleEq(isPossible)
                ).limit(20L)
                .offset(0L)
                .fetch();
    }

    private BooleanExpression characterTypeEq(CharacterType type) {
        return type != null ? character.type.eq(type) : null;
    }
    private BooleanExpression isPossibleEq(Boolean isPossible) {
        return isPossible != null ? character.isPossible.eq(isPossible) : null;
    }
}
