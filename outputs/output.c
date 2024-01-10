#include <iostream> 
using namespace std; 
int main() { 
    int v[10];
    int i;
    i = 0;
    while (i < 10) {
            cout << "v[";
            cout << i;
            cout << "]=?";
            cin >> v[i];
            i = i + 1;
    }
    int gata;
    gata = -1;
    while (gata < 0) {
            gata = 1;
            i = 0;
            while (i < 9) {
                    if (v[i] > v[i + 1]) {
                            int x;
                            x = v[i];
                            v[i] = v[i + 1];
                            v[i + 1] = x;
                            gata = -1;
                    }
                    i = i + 1;
            }
    }
    cout << "v sortat:" << endl;
    i = 0;
    while (i < 9) {
            cout << v[i];
            cout << ",";
            i = i + 1;
    }
    cout << v[9];
    cout << ".";
}
