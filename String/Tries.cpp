#include <string>
using namespace std;


class TrieNode{
private:
    TrieNode* next[26];
    bool isWord;
public:
    TrieNode(){
        memset(next,0,sizeof(next));
        isWord = false;
    }
};

class Tries{
private:
    TrieNode* root;

    TrieNode* find(string word){
        TrieNode*p = root;
        for(int i=0;i<word.size()&&p!=NULL;i++){
            p = p->next[word[i]-'a'];
        }
        return p;
    }
public:
    Tries(){
        root = new TrieNode();
    }

    void insert(string word){
        TrieNode*p = root;
        for(int i=0;i<word.size();i++){
            if(p->next[word[i]-'a']==NULL){
                p->next[word[i]-'a'] = new TrieNode();
            }
            p = p->next[word[i]-'a'];
        }
        p->isWord =true;
    }

    bool search(string word){
        TrieNode*p = find(word);
        return p!=NULL &&p->isWord;
    }

    bool startsWithPrefix(string prefix){
        return find(prefix)!=NULL;
    }

}