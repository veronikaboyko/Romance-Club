package org.example.keyboard;

/**
 * состояния бота, используемые для передвижению по логике работы бота
 * Restart - начальное
 * Story - состоянии на шаге вывода историй
 * Seasonss - состоянии на шаге вывода сезонов
 * Episode - состоянии на шаге вывода епизодов
 * Text - состояние при работе с конкретной историе
 * метод nextState - для перехода между состояниями
 */
public enum FinalStateAutomate {
    /**
     * состояние при вызове /restart, которое возвращает в начало работы бота
     */
    Restart{
        @Override
        public FinalStateAutomate nextState(String string) {
            return Story;
        }
    },
    /**
     * начальное состояние при вызове комнады /start
     */
    Start{
        @Override
        public FinalStateAutomate nextState(String string) {
            return Story;
        }
    },
    /**
     * состояние при работе с историей
     */
    Story{
        @Override
        public FinalStateAutomate nextState(String string) {
            if(string.equals("/restart")){
                return Restart;
            }
            return Seasonss;
        }
    },
    /**
     * состояние при работе с сезонами
     */
    Seasonss{
        @Override
        public FinalStateAutomate nextState(String string) {
            if(string.equals("/restart")){
                return Restart;
            }
            return Episode;
        }
    },
    /**
     * состояние при работе с эпизодами
     */
    Episode{
        @Override
        public FinalStateAutomate nextState(String string) {
            if(string.equals("/restart")){
                return Restart;
            }
            return Text;
        }
    },
    /**
     * состояние при работе с текстом конкретной истории
     */
    Text{
        @Override
        public FinalStateAutomate nextState(String string) {
            if(string.equals("/restart")){
                return Restart;
            }
            return this;
        }
    };

    /**
     * абстрактный метод который переводит текущее состояние в следующее
     * @param string - аргумент принимающий определенную строчку( в данном случаи /restart)
     */
    public abstract FinalStateAutomate nextState(String string);
}