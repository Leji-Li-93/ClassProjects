#include <iostream>
#include <stdlib.h>
#include <ctime>
#include <string>
#include <cmath>
#include <conio.h>

using namespace std;

int NUM_TRY = 3;
string message = "Play!";

const int HEAD = 0;
const int HAND_LEFT = 1;
const int BODY = 2;
const int HAND_RIGHT = 3;
const int LEG_LEFT = 4;
const int LEG_RIGHT = 5;

const char HEAD_CHAR = 'o';
const char HAND_LEFT_CHAR = '/';
const char BODY_CHAR = '|';
const char HAND_RIGHT_CHAR = '\\';
const char LEG_LEFT_CHAR = '/';
const char LEG_RIGHT_CHAR = '\\';

string man_map[]=
    {
        "  -------",
        "  |  |",
        "  |  o",
        "  | /|\\",
        "  | / \\",
        "  |",
        "-----------"
    };
int body_map[6][2]={{2,5},{3,4},{3,5},{3,6},{4,4},{4,6}};
int body_map_size = 6;

void main_menu();
void print_star(int);
bool checkGuess(char, string, string&);
void game_logic();

void init_hangman_man_map();
void draw_map();
void set_part();

int main()
{
    bool flag = true;
    while(flag){
        game_logic();
        cout << "Press \"Y\" to Restart, or Press \"N\" to Exit" << endl;
        while(true){
            char key = getch();
            if (key == 'Y' || key == 'y'){
                flag = true;
                break;
            }
            else if (key == 'N' || key == 'n'){
                flag = false;
                break;
            }
        }
    }
    return 0;
}

void main_menu()
{
    system("color 70");
    system("cls");
    /*for (int i = 1; i <= 7; i++)
        print_star(i);
    for (int i = 7; i>= 1; i--)
        print_star(i);*/

    init_hangman_man_map();
    draw_map();

    cout << "\t\t";
    for (int i = 0; i < 43; i++)
        cout << "@";
    cout << endl;

    cout << "\t\t\tHangman Game!" << endl;
    cout << "\t\tYou have " << NUM_TRY << " tries to try and guest the month." << endl;
    cout << "\n\t\t\t\t" + message;

    cout << "\n\t\t";
    for (int i = 0; i < 43; i++)
        cout << "@";
    cout << endl;
}

void print_star(int num)
{
    cout << "\t\t\t\t";
    for (int i = 0; i < num; i++){
        cout << "*";
    }
    cout << "\t";
    for (int i = 0; i <= num; i++){
        cout << "*";
    }
    cout << endl;
}

bool checkGuess(char guess, string secretmonth, string &guessmonth)
{
    int len = secretmonth.length();
    for (int i = 0; i < len; i++){
        if (guess == guessmonth[i])
            return false;
        if (guess == secretmonth[i])
        {
            guessmonth[i] = guess;
        }
    }
    return true;
}

void game_logic()
{
    char letter;
    string month;

    string months[]=
    {
        "january",
        "february",
        "march",
        "april",
        "may",
        "june",
        "july",
        "august",
        "september",
        "october",
        "november",
        "december"
    };

    srand(time(NULL));
    int n = rand()%12;
    month = months[n];
    string hide_m(month.length(), 'X');

    while(NUM_TRY != 0)
    {
        main_menu();

        cout << "\n\n\t\t\t\t" << hide_m << endl;
        cout << "\n\t\t\t\tGuess a letter: ";
        cin >> letter;

        if(!checkGuess(letter, month, hide_m)){
            message = "Incorrect letter.";
            NUM_TRY--;
        } else {
            message = "NICE! You guess a letter";
        }

        if (month == hide_m){
            message = "Congratulations! You got it!";
            main_menu();
            cout << "\n\t\t\t\tThe month is :" << month << endl;
            break;
        }
    }

    if (NUM_TRY == 0){
        message = "NOOOOOOOOOO!...you've been hanged.";
        main_menu();
        cout << "\n\t\t\t\tThe month was: " << month << endl;
    }

    cin.ignore();
    cin.get();
}

void draw_map()
{
    for (int i = 0; i < 7; i++){
        cout << man_map[i] << endl;
    }
}

void init_hangman_man_map()
{
    int x,y;
    string* tmp;
    for (int i = 0; i < body_map_size; i++){
        x = body_map[i][0];
        y = body_map[i][1];
        tmp = &man_map[x];
        //cout << "X - ++++" << man_map[x] << "====" <<endl;
        //cout << "Y - ++++" << tmp[y] << "====" <<endl;
        tmp[y] = ' ';
    }
}

void set_part(int part, char part_char)
{
    int x,y;
    string tmp;
    x = body_map[part][0];
    y = body_map[part][1];
    tmp = man_map[x];
    tmp[y] = part_char;
}
