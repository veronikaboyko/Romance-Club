package org.example.keyboard;

/**
 * состояния бота, используемые для передвижению по логике работы бота Restart - начальное Story -
 * состоянии на шаге вывода историй Seasonss - состоянии на шаге вывода сезонов Episode - состоянии
 * на шаге вывода епизодов Text - состояние при работе с конкретной историе метод nextState - для
 * перехода между состояниями.
 */
public enum FinalStateAutomate {
  /** состояние при вызове /restart, которое возвращает в начало работы бота. */
  RESTART {
    @Override
    public FinalStateAutomate nextState(String string) {
      return STORY;
    }
  },
  /** начальное состояние при вызове комнады /start. */
  START {
    @Override
    public FinalStateAutomate nextState(String string) {
      return STORY;
    }
  },
  /** состояние при работе с историей. */
  STORY {
    @Override
    public FinalStateAutomate nextState(String string) {
      if (string.equals("/restart")) {
        return RESTART;
      }
      return SEASONSS;
    }
  },
  /** состояние при работе с сезонами. */
  SEASONSS {
    @Override
    public FinalStateAutomate nextState(String string) {
      if (string.equals("/restart")) {
        return RESTART;
      }
      return EPISODE;
    }
  },
  /** состояние при работе с эпизодами. */
  EPISODE {
    @Override
    public FinalStateAutomate nextState(String string) {
      if (string.equals("/restart")) {
        return RESTART;
      }
      return TEXT;
    }
  },
  /** состояние при работе с текстом конкретной истории. */
  TEXT {
    @Override
    public FinalStateAutomate nextState(String string) {
      if (string.equals("/restart")) {
        return RESTART;
      }
      return this;
    }
  };

  /**
   * абстрактный метод который переводит текущее состояние в следующее.
   *
   * @param string - аргумент принимающий определенную строчку( в данном случаи /restart)
   */
  public abstract FinalStateAutomate nextState(String string);
}
