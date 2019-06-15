package lotto;

import lotto.db.dao.LottoDAO;
import lotto.db.dao.WinningLottoDAO;
import lotto.db.dto.LottoGameResultDTO;
import lotto.domain.*;
import lotto.domain.Factory.LottoTicketsFactory;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.*;

import static spark.Spark.*;

public class WebUILottoApplication {
    public static void main(String[] args) {
        int week = 1;

        staticFiles.location("/");

        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return render(model, "index.html");
        });

        path("/purchase", () -> {
            get("", (req, res) -> {
                Map<String, Object> model = new HashMap<>();
                return render(model, "purchasing_lotto.html");
            });

            post("/ticket", (req, res) -> {
                Map<String, Object> model = new HashMap<>();
                Money money = new Money(Integer.parseInt(req.queryParams("money")));
                List<String> inputCustoms = Arrays.asList(req.queryParams("lottos").split("-"));
                LottoTickets lottoTickets = LottoTicketsFactory.getInstance().create(money, inputCustoms);

                model.put("money", money.getMoney());
                model.put("lottos", lottoTickets.getLottoTickets());
                model.put("amountOfCustom", inputCustoms.size());
                model.put("amountOfAuto", (money.getMoney() / 1000) - inputCustoms.size());

                LottoDAO.addLottoTicket(lottoTickets);

                return render(model, "purchased_tickets.html");
            });
        });

        path("/win", () -> {
            post("/input", (req, res) -> {
                StringBuilder lottoNumbers = new StringBuilder();
                for (int i = 1; i <= 6; i++) {
                    lottoNumbers.append(req.queryParams("num" + i)).append(",");
                }
                WinningLottoDAO.addWinningLottoTicket(WinningLotto.of(lottoNumbers.toString(), Integer.parseInt(req.queryParams("bonusBall"))));

                res.redirect("/");
                return null;
            });

            get("/result", (req, res) -> {
                Map<String, Object> model = new HashMap<>();
                LottoGameResultDTO winningLotto = WinningLottoDAO.findLatestWinningLotto();
                List<LottoTicket> lottoTickets = LottoDAO.findLottosByLottoId(winningLotto.getWinningLottoId());
                WinStatistics winStatistics = new WinStatistics(lottoTickets, WinningLotto.of(winningLotto.getWinningNumbers(), winningLotto.getBonusBall()));

                model.put("week", winningLotto.getWinningLottoId());
                model.put("winningNumbers", winningLotto.getWinningNumbers().split(","));
                model.put("bonusBall", winningLotto.getBonusBall());
                model.put("results", getEachRank(winStatistics));
                model.put("incoming_rate", String.format("%.2f", winStatistics.calculateProfitRate(lottoTickets.size() * 1000)));

                return render(model, "lotto_result.html");
            });
        });
    }

    private static List<String> getEachRank(WinStatistics winStatistics) {
        List<String> results = new ArrayList<>();
        for (RankType rankType : RankType.values()) {
            int matchingCount = rankType.getMatchingCount();
            int prize = rankType.getPrize();
            int count = winStatistics.getCountOfResult().get(rankType);

            if (rankType.equals(RankType.SECOND)) {
                results.add(String.format("%d개 일치, 보너스 볼 일치(%d원) - %d개\n", matchingCount, prize, count));
                continue;
            }
            if (rankType.equals(RankType.NOTHING)) {
                continue;
            }
            results.add(String.format("%d개 일치 (%d원)- %d개\n", matchingCount, prize, count));
        }
        return results;
    }

    private static String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}
