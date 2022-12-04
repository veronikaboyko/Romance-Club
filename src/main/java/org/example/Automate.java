package org.example.keyboard;

public enum Automate {
    Story{
        public boolean[] SetFlags(){
            return new boolean[]{true,false,false,false};
        }
    },
    Seasonss{
        public boolean[] SetFlags(){
            return new boolean[]{false,true,false,false};
        }
    },Episode{
        public boolean[] SetFlags(){
            return new boolean[]{false,false,true,false};
        }
    },Text{
        public boolean[] SetFlags(){
            return new boolean[]{false,false,false,true};
        }
    };

    public abstract boolean[] SetFlags();
};