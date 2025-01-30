package kr.hhplus.be.server.interfaces.balance;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.application.balance.BalanceFacade;
import kr.hhplus.be.server.application.balance.request.BalanceInfo;
import kr.hhplus.be.server.application.balance.response.BalanceResult;
import kr.hhplus.be.server.interfaces.balance.request.BalanceRequest;
import kr.hhplus.be.server.interfaces.balance.response.BalanceResponse;
import kr.hhplus.be.server.support.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
@Tag(name = "BalanceController", description = "잔액")
public class BalanceController {

    private final BalanceFacade balanceFacade;

    @Operation(
            summary = "잔액 조회",
            description = "특정 사용자의 잔액을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "잔액 조회 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BalanceResult.BalanceRegisterV1.class))),
                    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping("/balances/{userId}")
    @Parameter(name = "userId", description = "사용자 ID", example = "1")
    public CustomApiResponse<BalanceResponse.UserBalanceResponse> getUserBalance(@PathVariable (name="userId") long userId) {
        BalanceResult.BalanceRegisterV1 response =  balanceFacade.getUserBalance(new BalanceRequest.BalanceInfo(userId));
        return CustomApiResponse.ok(BalanceResponse.UserBalanceResponse.of(response), "잔액 조회에 성공했습니다.");
    }

    @Operation(
            summary = "잔액 충전",
            description = "특정 사용자의 잔액을 충전합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "잔액 충전을 위한 요청 정보",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BalanceInfo.BalanceRegisterV1.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "잔액 충전 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseEntity.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping("/balance/charge")
    public CustomApiResponse<BalanceResponse.UserBalanceResponse> chargeBalance(@RequestBody BalanceRequest.BalanceCharge balanceCharge) {
        BalanceResult.BalanceRegisterV1 response = balanceFacade.charge(BalanceRequest.BalanceCharge.from(balanceCharge));
        return CustomApiResponse.ok(BalanceResponse.UserBalanceResponse.of(response),  "잔액 충전 성공");
    }
}
