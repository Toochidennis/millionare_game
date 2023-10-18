package com.digitalDreams.millionaire_game;

class ChooseTheme {

    public void setColorTheme(){
        switch (Constant.color){
            case  0xFF6200EE:
                Constant.theme = R.style.Theme_Trivia;
                break;
            case 0xffb703:
                Constant.theme = R.style.Theme1;
                break;
        }
    }
}
