import enums.ActionLetter;
import model.*;
import util.UniversalArray;
import util.UniversalArrayImpl;

import java.util.Scanner;

public class AppRunner {

    private final UniversalArray<Product> products = new UniversalArrayImpl<>();

    private final CoinAcceptor coinAcceptor;

    private static boolean isExit = false;

    private AppRunner() {
        products.addAll(new Product[]{
                new Water(ActionLetter.B, 20),
                new CocaCola(ActionLetter.C, 50),
                new Soda(ActionLetter.D, 30),
                new Snickers(ActionLetter.E, 80),
                new Mars(ActionLetter.F, 80),
                new Pistachios(ActionLetter.G, 130)
        });
        coinAcceptor = new CoinAcceptor(100);
    }

    public static void run() {
        AppRunner app = new AppRunner();
        while (!isExit) {
            app.startSimulation();
        }
    }

    private void startSimulation() {
        print("== == == == == == == ==");
        print("В автомате доступны:");
        showProducts(products);

        print("Монет на сумму: " + coinAcceptor.getAmount());

        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        allowProducts.addAll(getAllowedProducts().toArray());
        chooseAction(allowProducts);

    }

    private UniversalArray<Product> getAllowedProducts() {
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        for (int i = 0; i < products.size(); i++) {
            if (coinAcceptor.getAmount() >= products.get(i).getPrice()) {
                allowProducts.add(products.get(i));
            }
        }
        return allowProducts;
    }

    private void chooseAction(UniversalArray<Product> products) {
        print("== == == == == == == == ");
        print(" a - Пополнить баланс");
        showActions(products);
        print(" h - Выйти");
        print("== == == == == == == == ");
        System.out.printf("Выберите способ оплаты:\nc - картой\nn - наличными\n");
        String paymentMethod = fromConsole().substring(0, 1);

        if ("a".equalsIgnoreCase(paymentMethod)) {
            coinAcceptor.setAmount(coinAcceptor.getAmount() + 10);
            print("Вы пополнили баланс на 10");
            return;
        }

        if ("c".equalsIgnoreCase(paymentMethod)) {
            handleCardPayment();
        } else if ("n".equalsIgnoreCase(paymentMethod)) {
            handleCashPayment(products);
        } else {
            print("Недопустимый способ оплаты. Попробуйте еще раз.");
            chooseAction(products);
        }
    }

    private void handleCardPayment() {
        print("Введите номер счета:");
        String accountNumber = fromConsole();
        print("Введите пин-код:");
        String pinCode = fromConsole();

        print("Введите сумму для пополнения баланса карты:");
        double topUpAmount = Double.parseDouble(fromConsole());

        // Add logic to handle card payment
        // You can check the validity of the account number and pin code
        // and deduct the appropriate amount from the account.

        // For demonstration purposes, let's assume the card payment is successful
        coinAcceptor.setAmount((int) (coinAcceptor.getAmount() + topUpAmount));

        print("Ваша карта успешно пополнена!");
        print("Остаток на балансе: " + coinAcceptor.getAmount());
    }



    private void handleCashPayment(UniversalArray<Product> products) {
        showActions(products);
        String action = fromConsole().substring(0, 1);
        try {
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getActionLetter().equals(ActionLetter.valueOf(action.toUpperCase()))) {
                    coinAcceptor.setAmount(coinAcceptor.getAmount() - products.get(i).getPrice());
                    print("Вы купили " + products.get(i).getName());
                    break;
                }
            }
        } catch (IllegalArgumentException e) {
            print("Недопустимая буква. Попробуйте еще раз.");
            handleCashPayment(products);
        }
    }

    private void showActions(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(String.format(" %s - %s", products.get(i).getActionLetter().getValue(), products.get(i).getName()));
        }
    }

    private String fromConsole() {
        return new Scanner(System.in).nextLine();
    }

    private void showProducts(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(products.get(i).toString());
        }
    }

    private void print(String msg) {
        System.out.println(msg);
    }
}
