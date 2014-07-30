/*
 * JEF - Copyright 2009-2010 Jiyi (mr.jiyi@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jef.database.jsqlparser.statement;

import jef.database.jsqlparser.statement.create.table.CreateTable;
import jef.database.jsqlparser.statement.delete.Delete;
import jef.database.jsqlparser.statement.drop.Drop;
import jef.database.jsqlparser.statement.insert.Insert;
import jef.database.jsqlparser.statement.replace.Replace;
import jef.database.jsqlparser.statement.select.Select;
import jef.database.jsqlparser.statement.truncate.Truncate;
import jef.database.jsqlparser.statement.update.Update;

public interface StatementVisitor {

    public void visit(Select select);

    public void visit(Delete delete);

    public void visit(Update update);

    public void visit(Insert insert);

    public void visit(Replace replace);

    public void visit(Drop drop);

    public void visit(Truncate truncate);

    public void visit(CreateTable createTable);
}
