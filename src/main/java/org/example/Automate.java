package org.example.keyboard;

/**
 * состояния бота
 * Restart - начальное
 * Story - состоянии на шаге вывода историй
 * Seasonss - состоянии на шаге вывода сезонов
 * Episode - состоянии на шаге вывода епизодов
 * Text - состояние при работе с конкретной историе
 * метод nextState - для перехода между состояниями
 */
public enum Automate {
    Restart{
        @Override
        public Automate nextState(String string) {
            return Story;
        }
    },
    Start{
        @Override
        public Automate nextState(String string) {
            return Story;
        }
    },
    Story{
        @Override
        public Automate nextState(String string) {
            if(string.equals("/restart")){
                return Restart;
            }
            return Seasonss;
        }
    },
    Seasonss{
        @Override
        public Automate nextState(String string) {
            if(string.equals("/restart")){
                return Restart;
            }
            return Episode;
        }
    },
    Episode{
        @Override
        public Automate nextState(String string) {
            if(string.equals("/restart")){
                return Restart;
            }
            return Text;
        }
    },
    Text{
        @Override
        public Automate nextState(String string) {
            if(string.equals("/restart")){
                return Restart;
            }
            return this;
        }
    };
    public abstract Automate nextState(String string);
    }
