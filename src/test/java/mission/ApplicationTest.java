package mission;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

import api.TestEnvironment;
import java.util.List;
import mission.application.domain.exception.FishNotFound;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import java.time.Duration;

public class ApplicationTest extends TestEnvironment {
    @Test
    void 생존일_단순케이스_4일() {
        run(List.of("[삼치-1],[고등어-1],[멸치-1],[플랑크톤-3]"));
        assertTrue(output().contains("4일간 생존했습니다."));
    }

    @Test
    void 생존일_순차사망_정어리만남음() {
        run(List.of("[플랑크톤-15],[정어리-3],[멸치-3],[꽁치-2]"));
        assertTrue(output().contains("6일간 생존했습니다."));
    }

    @Test
    void 존재하지_않는_어류_입력시_IllegalArgumentException_발생() {
        assertThrows(FishNotFound.class, () -> {
            run(List.of("[갸라도스-1]"));
        });
    }


    @Test
    void 잘못된_입력형식일때_IllegalArgumentException_발생() {
        assertThrows(IllegalStateException.class, () -> {
            run(List.of("정어리-5, 고등어-2"));
        });
    }

    @Test
    void 정어리_1마리와_플랑크톤_1000마리_정상_생존() {
        run(List.of("[정어리-1],[플랑크톤-1000]"));
        assertTrue(output().contains("1001일간 생존했습니다."));
    }

    @Test
    void 플랑크톤만_있으면_0일생존() {
        run(List.of("[플랑크톤-100]"));
        assertTrue(output().contains("0일간 생존했습니다."));
    }

    @Test
    void 고등어만_있으면_1일생존() {
        run(List.of("[고등어-3]"));  // 먹이 없음
        assertTrue(output().contains("1일간 생존했습니다."));
    }

    @Test
    void 대규모_입력도_3분_내에_완료되어야_함() {
        assertTimeout(Duration.ofMinutes(1), () -> {
            run(List.of("[플랑크톤-300],[멸치-3],[정어리-3],[빙어-3],[꽁치-2],[전갱이-2],[고등어-2],[삼치-1]"));
            assertTrue(output().contains("일간 생존했습니다."));
        });
    }

    @Test
    void 대규모_입력도_3분_내에_완료되어야_함2() {
        assertTimeout(Duration.ofMinutes(1), () -> {
            run(List.of("[플랑크톤-200],[멸치-5],[정어리-5],[고등어-3]"));
            assertTrue(output().contains("일간 생존했습니다."));
        });
    }

    @Test
    void 약8배_복잡한_케이스_3분이내() {
        assertTimeout(Duration.ofMinutes(3), () -> {
            run(List.of("[플랑크톤-500],[멸치-3],[정어리-3],[빙어-3],[꽁치-1],[전갱이-1],[고등어-1],[삼치-1],[참치-1]"));
            assertTrue(output().contains("일간 생존했습니다."));
        });
    }

    @Test
    void 예외발생테스트() {
        assertThrows(IllegalArgumentException.class, () -> run("잘못된 입력"));
    }

    @Override
    public void runMain() {
        Application.main(new String[]{});
    }
}
