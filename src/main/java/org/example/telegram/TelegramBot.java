package org.example.telegram;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.example.commands.CommandTable;
import org.example.commands.StateHandler;
import org.example.jpa.StatEntityRespository;
import org.example.jpa.StateEntity2Repo;
import org.example.jpa.UserEntity;
import org.example.jpa.UserService;
import org.example.keyboard.FinalStateAutomate;
import org.example.model.Episode;
import org.example.model.Season;
import org.example.model.Story;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/** класс телеграмм бота. */
public class TelegramBot extends TelegramLongPollingBot {
  /** переменная для итерации по истории. */
  public int count = 1;

  public StateEntity2Repo stateEntity2Repo;

  private final String token;
  SendMessage sm = new SendMessage();
  public Story story = new Story();
  public Season season;
  public Episode episode;
  public String list;
  public String theMostState;
  public int theMostCount = -1;
  public UserEntity entity = new UserEntity();
  /** начальное состояние бота. */
  public FinalStateAutomate automate = FinalStateAutomate.START;

  private final StatEntityRespository respository;

  public HandlerForStory handler = new HandlerForStory();

  @Override
  public String getBotUsername() {
    return "Tutorial bot";
  }

  UserService service;

  @Override
  public String getBotToken() {
    return token;
  }

  private void handleTextMessage(Message message, UserEntity user) {
    CommandTable commandTable = new CommandTable(this);
    switch (message.getText()) {
      case "/start" -> commandTable.handleStartCommand(message);
      case "/info" -> commandTable.handleInfoCommand(message);
      case "/check", "/check the most", "/add", "/check all state" -> commandTable
          .handleAdminCommand();
      default -> checkState(message, user);
    }
  }

  private void checkState(Message message, UserEntity userEntity) {
    //    automate = automate.nextState(message.getText());
    userEntity.setState(userEntity.getState().nextState(message.getText()));
    CommandTable commandTable = new CommandTable(this);
    StateHandler stateHandler = new StateHandler(this, commandTable, stateEntity2Repo);
    switch (userEntity.getState()) {
      case START:
        commandTable.handleRestartCommand(message);
        break;
      case RESTART:
        commandTable.handleRestartCommand(message);
        break;
      case STORY:
        stateHandler.storyState(message);
        break;
      case SEASONSS:
        stateHandler.seasonsState(message);
        break;
      case EPISODE:
        stateHandler.episodeState(message);
        break;
      case TEXT:
        switch (message.getText()) {
          case "next" -> commandTable.handleNextCommand(message);
          case "before" -> commandTable.handleBeforeCommand(message);
          case "/back" -> commandTable.handleBackCommand(message);
          default -> commandTable.handleRestartCommand(message);
        }
        break;
      default:
    }
    service.save(userEntity, null);
  }

  /**
   * метод работает с телеграмм ботом: проверяет есть ли новое сообщение в боте обрабатывает его и
   * выводит Inline клаваиатуру при запуске Выводит информацию об обновлении гайда и уточнение о
   * запуске бота.
   */
  @Override
  public void onUpdateReceived(Update update) {
    if (update.hasMessage() && update.getMessage().hasText()) {
      long chatId = update.getMessage().getChatId();
      handleTextMessage(update.getMessage(), entity);
      try {

        if (update.getMessage().getText().equals("/add")) {
          if (!service.existsByChatId(chatId)) {
            entity.setChatId(chatId);
            entity.setSubscribe(false);
            service.save(entity, chatId);
            sendMessage(update, "That's OK!");
          } else {
            sendMessage(update, "U are in table yet!");
          }
        }
        if (update.getMessage().getText().equals("/check")) {
          System.out.println(chatId);
          boolean checkAdmin = service.check(chatId);
          System.out.println(checkAdmin);
          if (checkAdmin) {
            StringBuilder b = new StringBuilder();
            String format = "%s : %s, ";
            for (FinalStateAutomate value : FinalStateAutomate.values()) {
              int a = respository.count(value.name());
              if (a > theMostCount) {
                theMostCount = a;
                theMostState = value.toString();
              }
              b.append(String.format(format, value.name(), a));
            }
            sendMessage(update, String.valueOf(b));
            //            sendMessage(update, "You are the admin");
          } else {
            sendMessage(update, "You are not admin");
          }
        } else if (update.getMessage().getText().equals("/check the most")) {
          sendMessage(update, theMostState);
        } else if (update.getMessage().getText().equals("/check all state")) {
          List<Object> states = stateEntity2Repo.getList();
          states.stream()
              .map(e -> (Object[]) e)
              .filter(this::checkNull)
              .forEach(e -> sendMessage(update, Arrays.toString(e)));
        }
      } catch (Exception e) {
        sendMessage(update, "You are not admin");
      }
    } else if (update.hasCallbackQuery()) {
      handleCallbackQuery(update.getCallbackQuery());
    }
  }

  private boolean checkNull(Object[] objects) {
    for (Object obj : objects) {
      if (obj == null) return false;
    }
    return true;
  }

  private void handleCallbackQuery(CallbackQuery callbackQuery) {
    String callData = callbackQuery.getData();
    long messageId = callbackQuery.getMessage().getMessageId();
    long chatId = callbackQuery.getMessage().getChatId();
    switch (callData) {
      case "да":
        String answer;
        try {
          answer = story.printTitles();
        } catch (FileNotFoundException e) {
          throw new RuntimeException(e);
        }
        EditMessageText newMessage = new EditMessageText();
        newMessage.setChatId(chatId);
        newMessage.setMessageId(Math.toIntExact(messageId));
        newMessage.setText(answer);
        try {
          execute(newMessage);
        } catch (TelegramApiException e) {
          e.printStackTrace();
        }
        break;
      case "Когда следующее обновление?":
        newMessage = new EditMessageText();
        newMessage.setChatId(chatId);
        newMessage.setMessageId(Math.toIntExact(messageId));
        newMessage.setText("10-12 мая 2023 года");
        try {
          execute(newMessage);
        } catch (TelegramApiException e) {
          e.printStackTrace();
        }
        break;
      default:
    }
  }

  /** телеграмм токен спрятный в properties. */
  public TelegramBot(
      String token,
      String botName,
      UserService service,
      StatEntityRespository respository,
      StateEntity2Repo stateEntity2Repo)
      throws IOException {
    this.token = token;
    this.service = service;
    this.respository = respository;
    this.stateEntity2Repo = stateEntity2Repo;
  }

  /**
   * логика бота Проверка состояния бота и в зависимости от состояния выводит необходимую
   * информацию.
   */
  public void sendMessage(Update update, String text) {
    try {
      execute(
          SendMessage.builder()
              .chatId(
                  (update.hasMessage())
                      ? update.getMessage().getChatId()
                      : update.getCallbackQuery().getFrom().getId())
              .text(text)
              .build());
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }
}
