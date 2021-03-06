/*
 *
 *  * Copyright 2010-2016 OrientDB LTD (http://orientdb.com)
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.orientechnologies.lucene.query;

import com.orientechnologies.common.exception.OException;
import com.orientechnologies.lucene.exception.OLuceneIndexException;
import com.orientechnologies.lucene.tx.OLuceneTxChanges;
import com.orientechnologies.orient.core.command.OCommandContext;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.highlight.TextFragment;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

/**
 * Created by Enrico Risa on 08/01/15.
 */
public class OLuceneQueryContext {

  private final OCommandContext               context;
  private final IndexSearcher                 searcher;
  private final Query                         query;
  private final Sort                          sort;
  private final QueryContextCFG               cfg;
  private       Optional<OLuceneTxChanges>    changes;
  private       HashMap<String, TextFragment[]> fragments;

  public OLuceneQueryContext(OCommandContext context, IndexSearcher searcher, Query query) {
    this(context, searcher, query, null);
  }

  public OLuceneQueryContext(OCommandContext context, IndexSearcher searcher, Query query, Sort sort) {
    this.context = context;
    this.searcher = searcher;
    this.query = query;
    this.sort = sort;
    if (sort != null)
      cfg = QueryContextCFG.SORT;
    else
      cfg = QueryContextCFG.FILTER;

    changes = Optional.empty();
    fragments = new HashMap<>();
  }

  public boolean isInTx() {
    return changes.isPresent();
  }

  public OLuceneQueryContext withChanges(OLuceneTxChanges changes) {
    this.changes = Optional.ofNullable(changes);
    return this;
  }

  public OLuceneQueryContext addHighlightFragment(String field, TextFragment[] fieldFragment) {
    fragments.put(field,fieldFragment);

    return this;
  }

  public OCommandContext getContext() {
    return context;
  }

  public Query getQuery() {
    return query;
  }

  public Sort getSort() {
    return sort;
  }

  public IndexSearcher getSearcher() {

    return changes.map(c -> new IndexSearcher(multiReader(c)))
        .orElse(searcher);

  }

  private MultiReader multiReader(OLuceneTxChanges c) {
    try {
      return new MultiReader(searcher.getIndexReader(), c.searcher().getIndexReader());
    } catch (IOException e) {
      throw OException.wrapException(new OLuceneIndexException("unable to create reader on changes"), e);
    }
  }

  public QueryContextCFG getCfg() {
    return cfg;
  }

  public int deletedDocs(Query query) {

    return changes.map(c -> c.deletedDocs(query)).orElse(0);
  }

  public boolean isUpdated(Document doc, Object key, OIdentifiable value) {

    return changes.map(c -> c.isUpdated(doc, key, value)).orElse(false);

  }

  public boolean isDeleted(Document doc, Object key, OIdentifiable value) {

    return changes.map(c -> c.isDeleted(doc, key, value)).orElse(false);

  }

  public HashMap<String, TextFragment[]> getFragments() {
    return fragments;
  }

  public enum QueryContextCFG {
    FILTER, SORT
  }

}
